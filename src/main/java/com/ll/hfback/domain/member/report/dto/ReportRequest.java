package com.ll.hfback.domain.member.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
  @NotNull(message = "신고 대상자 ID는 필수입니다.")
  private Long reportedId;

  @NotBlank(message = "신고 내용은 필수입니다.")
  private String content;
}
