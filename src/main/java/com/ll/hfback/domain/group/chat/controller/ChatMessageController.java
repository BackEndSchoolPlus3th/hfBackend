package com.ll.hfback.domain.group.chat.controller;

import com.ll.hfback.domain.group.chat.dto.response.ResponseMessage;
import com.ll.hfback.domain.group.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/groups/{roomId}")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    
    @PostMapping("/messages") // 채팅 입력
    public void writeMessage(@PathVariable Long roomId,
                             @RequestBody ResponseMessage responseMessage) {
        chatMessageService.writeMessage(roomId,
                responseMessage.getNickname(),  // fix: 멤버 ID로 변경할 것 (엔티티가 없어서 현재는 임시방편)
                responseMessage.getContent());
    }
}