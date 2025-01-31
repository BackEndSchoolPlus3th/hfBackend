package com.ll.hfback.domain.member.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReportListResponse {
  private List<ReportResponse> reports;
  private long totalReports;
}
