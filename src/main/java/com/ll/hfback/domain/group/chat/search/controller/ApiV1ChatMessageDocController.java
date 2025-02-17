package com.ll.hfback.domain.group.chat.search.controller;

import com.ll.hfback.domain.group.chat.request.MessageSearchKeywordsRequest;
import com.ll.hfback.domain.group.chat.response.ResponseMessage;
import com.ll.hfback.domain.group.chat.search.service.ChatMessageDocService;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.global.rsData.RsData;
import com.ll.hfback.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.ll.hfback.domain.group.chat.search.controller
 * fileName       : ApiV1ChatMessageDocController
 * author         : sungjun
 * date           : 2025-02-10
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-10        kyd54       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/chatRooms/{chatRoom-id}")
public class ApiV1ChatMessageDocController {

    private final ChatMessageDocService chatMessageDocService;
    @GetMapping("/messages/search")
    public RsData<Page<ResponseMessage>> searchMessages (@PathVariable("chatRoom-id") Long chatRoomId,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @ModelAttribute MessageSearchKeywordsRequest messageSearchKeywordsRequest, @LoginUser Member loginUser) {

        try {
            Page<ResponseMessage> messages = chatMessageDocService.searchMessages(chatRoomId, page, messageSearchKeywordsRequest, loginUser);

            return new RsData<>("200", "검색 조건에 따른 채팅 메시지 조회에 성공했습니다.", messages);
        } catch (Exception e) {
            return new RsData<>("500", "엘라스틱서치 채팅방 메시지 검색 실패");
        }
    }
}
