package com.ll.hfback.domain.member.report.repository;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {
  long countByReportedAndState(Member reported, Report.ReportState state);

  @Query("SELECT r FROM Report r " +
      "JOIN FETCH r.reporter " +
      "JOIN FETCH r.reported")
  Page<Report> findAllWithMembers(Pageable pageable);
}
