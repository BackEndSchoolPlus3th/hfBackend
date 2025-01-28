package com.ll.hfback.domain.group.chat.serviceImpl;

import com.ll.hfback.domain.group.chat.entity.MessageReadStatus;
import com.ll.hfback.domain.group.chat.entity.QChatMessage;
import com.ll.hfback.domain.group.chat.repository.MessageReadStatusRepository;
import com.ll.hfback.domain.group.chat.request.RequestMessage;
import com.ll.hfback.domain.group.chat.response.MessageReadStatusResponse;
import com.ll.hfback.domain.group.chat.response.MessageSearchKeywordsResponse;
import com.ll.hfback.domain.group.chat.response.ResponseMessage;
import com.ll.hfback.domain.group.chat.entity.ChatMessage;
import com.ll.hfback.domain.group.chat.repository.ChatMessageRepository;
import com.ll.hfback.domain.group.chat.service.ChatMessageService;
import com.ll.hfback.domain.group.chatRoom.entity.ChatRoom;
import com.ll.hfback.domain.group.chatRoom.repository.ChatRoomRepository;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.repository.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.ll.hfback.domain.group.chat.service
 * fileName       : ChatMessageService
 * author         : sungjun
 * date           : 2025-01-22
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-22        kyd54       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(ChatMessageServiceImpl.class.getName());
    private final ChatRoomRepository chatRoomRepository;
    private final MessageReadStatusRepository messageReadStatusRepository;

    // 채팅 메시지 작성
    @Transactional
    public void writeMessage(Long chatId, ResponseMessage responseMessage) {
        try {
            // 빈 메시지 또는 250자 초과 메시지 검사
            if (responseMessage.getContent() == null || responseMessage.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("채팅 메시지는 비어 있을 수 없습니다.");
            }
            if (responseMessage.getContent().length() > 250) {
                throw new IllegalArgumentException("채팅 메시지는 250자를 넘을 수 없습니다.");
            }

            // chatId로 채팅방 정보 가져오기
            ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElse(null);

            if(chatRoom != null) {
                // memberId로 멤버 정보 가져오기
                Member member = memberRepository.findById(responseMessage.getMemberId()).orElse(null);

                // 채팅 메시지 저장
                ChatMessage chatMessage = ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .nickname(member.getNickname())
                        .chatMessageContent(responseMessage.getContent())
                        .build();

                chatMessageRepository.save(chatMessage);

                // 지정된 채팅방으로 메시지 전송
                simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);

                logger.info("채팅 메시지 작성 성공");
            } else {
                logger.error("채팅 메시지 작성 실패, ChatRoom is null");
            }
            // fix: 어떤 에러가 발생 할 수 있는지, 에러 유형별 처리 방법 생각 (던지기 x)
            // 예외를 발생시켜 클라이언트에 알림, 필요하면 400 Bad Request와 같은 HTTP 응답 코드 설정
            // 또는 커스텀 예외를 던질 수 있음
        } catch (IllegalArgumentException e) {
            logger.error("채팅 메시지 작성 실패: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("채팅 메시지 작성 실패 : " + e);
            throw e;
        }
    }

    // 채팅 메시지 가져오기
    @Transactional(readOnly = true)
    public Page<RequestMessage> readMessages(Long chatRoomId, int page) {
        try {
            Page<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId, customPaging(page));
            logger.info("채팅 메시지 가져오기 성공");
            // ChatMessage -> RequestMessage 변환
            return chatMessages.map(chatMessage ->
                    new RequestMessage(chatMessage.getNickname(),
                            chatMessage.getChatMessageContent(),
                            chatMessage.getCreateDate(),
                            chatMessage.getId()
                    ));
        } catch (Exception e) {
            logger.info("채팅 메시지 가져오기 실패");
            throw e;
        }
    }

    // 고정된 페이지 크기 10으로 Pageable 객체를 생성 (메시지 불러올 때 사용할 커스텀 페이징)
    public Pageable customPaging(int page) {
        try {
            // 내림차순 정렬 (createdAt 기준)
            Sort sort = Sort.by(Sort.Order.desc("createDate"));
            logger.info("커스텀 페이징 성공");
            
            // 페이지 번호와 크기, 정렬을 포함하여 Pageable 객체 생성
            return PageRequest.of(page, 10, sort);
        } catch (Exception e) {
            logger.info("커스텀 페이징 실패");
            throw e;
        }
    }

    // 조건에 따른 채팅 메시지 검색 기능
    public Page<RequestMessage> searchMessages(Long chatRoomId,
                                               int page,
                                               MessageSearchKeywordsResponse messageSearchKeywordsResponse) {
        try {
            QChatMessage qChatMessage = QChatMessage.chatMessage; // QueryDSL 메타 모델 객체

            BooleanBuilder builder = new BooleanBuilder();

            // 검색어(keyword)가 있으면 해당 조건 추가
            if (messageSearchKeywordsResponse != null && messageSearchKeywordsResponse.getKeyword() != null) {
                String keyword = messageSearchKeywordsResponse.getKeyword();
                builder.and(qChatMessage.chatMessageContent.containsIgnoreCase(keyword)); // 대소문자 구분 없이 검색
            }

            // 닉네임(nickname)이 있으면 해당 조건 추가
            if (messageSearchKeywordsResponse.getNickname() != null) {
                String nickname = messageSearchKeywordsResponse.getNickname();
                builder.and(qChatMessage.nickname.containsIgnoreCase(nickname)); // 대소문자 구분 없이 검색
            }

            // 날짜 처리: 날짜를 LocalDateTime으로 변환하여 비교
            // startDate만 있을 경우
            if (messageSearchKeywordsResponse.getStartDate() != null) {
                LocalDate startDate = messageSearchKeywordsResponse.getStartDate();
                LocalDateTime startDateTime = startDate.atStartOfDay(); // startDate를 00:00:00로 변환
                builder.and(qChatMessage.createDate.goe(startDateTime)); // startDate부터 최근까지 메시지 검색
            }

            // endDate만 있을 경우
            if (messageSearchKeywordsResponse.getEndDate() != null) {
                LocalDate endDate = messageSearchKeywordsResponse.getEndDate();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999); // endDate를 23:59:59로 변환
                builder.and(qChatMessage.createDate.loe(endDateTime)); // 처음부터 endDate까지 메시지 검색
            }

            // startDate와 endDate 모두 있을 경우
            if (messageSearchKeywordsResponse.getStartDate() != null && messageSearchKeywordsResponse.getEndDate() != null) {
                LocalDate startDate = messageSearchKeywordsResponse.getStartDate();
                LocalDate endDate = messageSearchKeywordsResponse.getEndDate();
                LocalDateTime startDateTime = startDate.atStartOfDay(); // startDate를 00:00:00로 변환
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999); // endDate를 23:59:59로 변환
                builder.and(qChatMessage.createDate.between(startDateTime, endDateTime)); // 두 날짜 사이의 메시지 검색
            }

            // chatRoomId에 맞는 메시지 필터링
            builder.and(qChatMessage.chatRoom.id.eq(chatRoomId));

            // 페이지 처리와 함께 검색된 메시지 조회
            Page<ChatMessage> searchMessages = chatMessageRepository.findAll(builder, customPaging(page));

            logger.info("조건에 따른 채팅 메시지 검색 성공");
            // ChatMessage -> RequestMessage 변환
            return searchMessages.map(chatMessage ->
                    new RequestMessage(chatMessage.getNickname(),
                            chatMessage.getChatMessageContent(),
                            chatMessage.getCreateDate(),
                            chatMessage.getId()));
        } catch (Exception e) {
            logger.info("조건에 따른 채팅 메시지 검색 실패");
            throw e;
        }
    }

    // 메시지 읽음/안읽음 상태 확인
    @Transactional
    public void messageReadStatus(Long chatRoomId, MessageReadStatusResponse messageReadStatusResponse) {
        try {
            MessageReadStatus readStatus = messageReadStatusRepository
                    .findByChatRoomIdAndMemberId(chatRoomId, messageReadStatusResponse.getMemberId())
                    .orElse(MessageReadStatus.builder()
                            .chatRoom(chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found")))
                            .member(memberRepository.findById(messageReadStatusResponse.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found")))
                            .lastReadMessageId(messageReadStatusResponse.getMessageId())
                            .build());

            readStatus.setLastReadMessageId(messageReadStatusResponse.getMessageId());
            messageReadStatusRepository.save(readStatus);

            logger.info("ChatRoom ID: {}, Member ID: {} - 마지막 읽은 메시지 ID가 성공적으로 업데이트되었습니다.",
                    chatRoomId,
                    messageReadStatusResponse.getMemberId());
        } catch (Exception e) {
            logger.error("마지막 읽은 메시지 업데이트 실패");
            throw e;
        }

    }
}
