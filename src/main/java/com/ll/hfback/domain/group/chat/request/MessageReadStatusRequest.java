package com.ll.hfback.domain.group.chat.request;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.ll.hfback.domain.group.chat.response
 * fileName       : MessageReadStatusResponse
 * author         : sungjun
 * date           : 2025-01-28
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-28        kyd54       최초 생성
 */
@Getter
@Setter
public class MessageReadStatusRequest {
    private Long memberId;
    private Long messageId;
}
