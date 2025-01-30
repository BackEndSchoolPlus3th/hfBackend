package com.ll.hfback.domain.group.chat.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * packageName    : com.ll.hfback.domain.group.chat.response
 * fileName       : MessageSearchKeywordsResponse
 * author         : sungjun
 * date           : 2025-01-27
 * description    : 자동 주석 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-27        kyd54       최초 생성
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageSearchKeywordsRequest {
    private String keyword;
    private String nickname;
    private LocalDate startDate;
    private LocalDate endDate;
}
