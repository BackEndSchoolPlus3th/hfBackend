package com.ll.hfback.domain.member.report.service;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.service.MemberService;
import com.ll.hfback.domain.member.report.entity.Report;
import com.ll.hfback.domain.member.report.entity.Report.ReportState;
import com.ll.hfback.domain.member.report.repository.ReportRepository;
import com.ll.hfback.global.exceptions.ErrorCode;
import com.ll.hfback.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberService memberService;


    @Transactional
    public Report createReport(Long reporterId, Long reportedId, String content) {
        Member reporter = memberService.findById(reporterId)
            .orElseThrow(() -> new ServiceException(ErrorCode.REPORTER_NOT_FOUND));

        Member reported = memberService.findById(reportedId)
            .orElseThrow(() -> new ServiceException(ErrorCode.REPORTED_NOT_FOUND));

        if (reporter.equals(reported)) {
            throw new ServiceException(ErrorCode.REPORTER_SELF_REPORT);
        }

        Report report = Report.builder()
            .reporter(reporter)
            .reported(reported)
            .content(content)
            .state(ReportState.WAITING)
            .build();

        reporter.addReport(report);
        return reportRepository.save(report);
    }


    public Page<Report> getReports(Pageable pageable) {
        return reportRepository.findAllWithMembers(pageable);
    }


    @Transactional
    public void rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ServiceException(ErrorCode.REPORT_NOT_FOUND));

        report.reject();
    }


    @Transactional
    public void confirmReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new ServiceException(ErrorCode.REPORT_NOT_FOUND));

        report.confirm();

        long confirmCount = reportRepository.countByReportedAndState(
            report.getReported(),
            ReportState.CONFIRMED
        );

        if (confirmCount >= 3) {
            memberService.banMember(report.getReported().getId());
        }
    }
}
