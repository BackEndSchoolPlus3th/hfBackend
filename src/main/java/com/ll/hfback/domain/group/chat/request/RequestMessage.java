package com.ll.hfback.domain.group.chat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.ll.hfback.domain.group.chat.request
 * fileName       : RequestMessage
 * author         : sungjun
 * date           : 2025-01-23
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-23        kyd54       최초 생성
 */
@Setter
@AllArgsConstructor
public class RequestMessage {
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("chatMessageContent")
    private String content;
    @JsonProperty("messageTimestamp")
    private LocalDateTime messageTimestamp;
    @JsonProperty("messageId")
    private Long messageId;
}
