package com.ll.hfback.domain.member.report.dto;

import com.ll.hfback.domain.member.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReportResponse {
  private Long id;
  private String reporterNickname;
  private String reportedNickname;
  private String content;
  private LocalDateTime createdDate;

  public static ReportResponse of(Report report) {
    return ReportResponse.builder()
        .id(report.getId())
        .reporterNickname(report.getReporter().getNickname())
        .reportedNickname(report.getReported().getNickname())
        .content(report.getContent())
        .createdDate(report.getCreateDate())
        .build();
  }
}
