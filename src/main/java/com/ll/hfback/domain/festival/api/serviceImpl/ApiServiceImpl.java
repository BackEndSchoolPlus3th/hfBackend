package com.ll.hfback.domain.festival.api.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ll.hfback.domain.festival.api.service.ApiService;
import com.ll.hfback.domain.festival.post.entity.Post;
import com.ll.hfback.domain.festival.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {
    private final PostRepository postRepository;

    // Apis의 데이터를 Post에 저장
    @Override
    @Transactional
    public void saveForApis(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            List<Post> festivalList = new ArrayList<>();
            LocalDate today = LocalDate.now(); // 오늘 날짜 가져오기
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

            for (JsonNode item : items) {
                String festivalStartDateRaw = item.path("eventstartdate").asText();
                String festivalEndDateRaw = item.path("eventenddate").asText();

                // 날짜 변환
                LocalDate startDate = LocalDate.parse(festivalStartDateRaw, inputFormatter);
                LocalDate endDate = LocalDate.parse(festivalEndDateRaw, inputFormatter);

                // 오늘 날짜를 기준으로 지난 데이터는 건너뜀
                if (endDate.isBefore(today)) {
                    continue;
                }

                // 변환된 날짜를 "yyyy.MM.dd" 형식으로 저장
                String festivalStartDate = startDate.format(outputFormatter);
                String festivalEndDate = endDate.format(outputFormatter);

                Post entity = Post.builder()
                        .festivalId(item.path("contentid").asText())
                        .festivalName(item.path("title").asText())
                        .festivalStartDate(festivalStartDate)
                        .festivalEndDate(festivalEndDate)
                        .festivalArea(item.path("addr1").asText())
                        .festivalHallName(item.path("addr1").asText())
                        .festivalState(null)
                        .festivalUrl(item.path("firstimage").asText())
                        .createDate(LocalDateTime.now())
                        .modifyDate(LocalDateTime.now())
                        .genrenm("축제")
                        .inputType("APIS")
                        .build();
                festivalList.add(entity);
            }
            postRepository.saveAll(festivalList);
            log.info(festivalList.toString());
        } catch (Exception e) {
            throw new RuntimeException("API 응답 처리 중 오류 발생", e);
        }
    }

    // Kopis의 데이터를 Post에 저장
    @Override
    @Transactional
    public void saveForKopis(List<Post> posts) {
        postRepository.saveAll(posts);
    }

    // KOPIS에서 받아온 xml데이터를 Post객체로 변환
    @Override
    public List<Post> parseXmlToEntity(String xml) throws Exception {
        // XML을 Java 객체로 파싱하기 위한 XmlMapper 인스턴스를 생성
        XmlMapper xmlMapper = new XmlMapper();

        // XML 문자열을 Post 객체의 리스트로 파싱
        List<Post> kopisEntity = xmlMapper.readValue(xml, new TypeReference<List<Post>>() {});
        
        // 오늘 날짜를 기준으로 과거의 이벤트 필터링
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        // festivalEndDate가 오늘을 지난 포스트는 필터링
        kopisEntity.removeIf(post -> {
            LocalDate endDate = LocalDate.parse(post.getFestivalEndDate(), formatter);
            return endDate.isBefore(today);
        });

        kopisEntity.forEach(post -> {
            post.setCreateDate(LocalDateTime.now());
            post.setModifyDate(LocalDateTime.now());
            post.setInputType("KOPIS");
        });
        return kopisEntity;
    }
}
