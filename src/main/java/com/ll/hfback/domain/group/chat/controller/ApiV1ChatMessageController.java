package com.ll.hfback.domain.group.chat.controller;

import com.ll.hfback.domain.group.chat.request.RequestMessage;
import com.ll.hfback.domain.group.chat.response.MessageReadStatusResponse;
import com.ll.hfback.domain.group.chat.response.MessageSearchKeywordsResponse;
import com.ll.hfback.domain.group.chat.response.ResponseMessage;
import com.ll.hfback.domain.group.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.ll.hfback.domain.group.chat.controller
 * fileName       : ChatController
 * author         : sungjun
 * date           : 2025-01-21
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-21        kyd54       최초 생성
 */
@RestController
@RequestMapping("/api/v1/chatRooms/{chatRoom-id}")
@RequiredArgsConstructor
public class ApiV1ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 채팅 메시지 작성
    @PostMapping("/messages")
    public void writeMessage(@PathVariable("chatRoom-id") Long chatRoomId,
                             @RequestBody ResponseMessage responseMessage) {
        chatMessageService.writeMessage(chatRoomId,
                responseMessage);
    }

    // 채팅 메시지 조회
    @GetMapping("/messages")
    public Page<RequestMessage> readMessages(@PathVariable("chatRoom-id") Long chatRoomId,
                                             @RequestParam(value = "page", defaultValue = "0") int page) {

        return chatMessageService.readMessages(chatRoomId, page);
    }

    // 조건에 따른 채팅 메시지 검색 기능
    @GetMapping("/messages/search")
    public Page<RequestMessage> searchMessages(@PathVariable("chatRoom-id") Long chatRoomId,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestBody MessageSearchKeywordsResponse messageSearchKeywordsResponse) {

        return chatMessageService.searchMessages(chatRoomId, page, messageSearchKeywordsResponse);
    }


    // 메시지 읽음/안읽음 상태 확인용 필드 수정
    @PutMapping("/messages/readStatus")
    public void messageReadStatus(@PathVariable("chatRoom-id") Long chatRoomId,
                                  @RequestBody MessageReadStatusResponse messageReadStatusResponse) {
        chatMessageService.messageReadStatus(chatRoomId, messageReadStatusResponse);
    }
}
