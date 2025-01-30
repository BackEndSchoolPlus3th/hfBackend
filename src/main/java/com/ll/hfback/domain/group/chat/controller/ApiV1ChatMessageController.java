package com.ll.hfback.domain.group.chat.controller;

import com.ll.hfback.domain.group.chat.response.ResponseMessage;
import com.ll.hfback.domain.group.chat.request.MessageReadStatusRequest;
import com.ll.hfback.domain.group.chat.request.MessageSearchKeywordsRequest;
import com.ll.hfback.domain.group.chat.request.RequestMessage;
import com.ll.hfback.domain.group.chat.service.ChatMessageService;
import com.ll.hfback.global.rsData.RsData;
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
    public RsData<Void> writeMessage(@PathVariable("chatRoom-id") Long chatRoomId,
                      @RequestBody RequestMessage requestMessage) {
        try {
            chatMessageService.writeMessage(chatRoomId, requestMessage);
            return new RsData<>("200", "채팅 메시지 작성에 성공했습니다.");
        } catch (Exception e) {
            return new RsData<>("500", "채팅 메시지 작성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 채팅 메시지 조회
    @GetMapping("/messages")
    public RsData<Page<ResponseMessage>> readMessages(@PathVariable("chatRoom-id") Long chatRoomId,
                                                @RequestParam(value = "page", defaultValue = "0") int page) {
        try {
            return new RsData<Page<ResponseMessage>>("200","채팅 메시지 조회가 성공했습니다.", chatMessageService.readMessages(chatRoomId, page));
        } catch (Exception e) {
            return new RsData<>("500", "채팅 메시지 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 조건에 따른 채팅 메시지 검색 기능
    @GetMapping("/messages/search")
    public RsData<Page<ResponseMessage>> searchMessages(@PathVariable("chatRoom-id") Long chatRoomId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestBody MessageSearchKeywordsRequest messageSearchKeywordsRequest) {
        try {
            return new RsData<Page<ResponseMessage>>("200", "검색 조건에 따른 채팅 메시지 조회에 성공했습니다.", chatMessageService.searchMessages(chatRoomId, page, messageSearchKeywordsRequest));
        } catch (Exception e) {
            return new RsData<>("500", "검색 조건에 따른 채팅 메시지 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    // 메시지 읽음/안읽음 상태 확인용 필드 수정
    @PutMapping("/messages/readStatus")
    public RsData<Void> messageReadStatus(@PathVariable("chatRoom-id") Long chatRoomId,
                                  @RequestBody MessageReadStatusRequest messageReadStatusRequest) {
        try {
            chatMessageService.messageReadStatus(chatRoomId, messageReadStatusRequest);
            return new RsData<>("200", "메시지 수신 상태 변경 완료");
        } catch (Exception e) {
            return new RsData<>("500", "메시지 수신 상태 변경 실패" + e.getMessage());
        }
    }
}
