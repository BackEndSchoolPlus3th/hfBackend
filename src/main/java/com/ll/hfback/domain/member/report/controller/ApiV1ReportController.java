package com.ll.hfback.domain.member.report.controller;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.entity.Member.MemberRole;
import com.ll.hfback.domain.member.member.repository.MemberRepository;
import com.ll.hfback.domain.member.member.service.MemberService;
import com.ll.hfback.domain.member.report.dto.ReportListResponse;
import com.ll.hfback.domain.member.report.dto.ReportRequest;
import com.ll.hfback.domain.member.report.dto.ReportResponse;
import com.ll.hfback.domain.member.report.entity.Report;
import com.ll.hfback.domain.member.report.service.ReportService;
import com.ll.hfback.global.exceptions.ErrorCode;
import com.ll.hfback.global.exceptions.ServiceException;
import com.ll.hfback.global.rsData.RsData;
import com.ll.hfback.global.webMvc.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ApiV1ReportController {

    private final ReportService reportService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    // MEM04_REPORT01 : 회원 신고하기
    @PostMapping
    public RsData<ReportResponse> createReport(
        @Valid @RequestBody ReportRequest request,
        @LoginUser Member loginUser
    ) {
        Report report = reportService.createReport(
            loginUser.getId(), request.getReportedId(), request.getContent()
        );

        return new RsData<>(
            "200-1",
            "신고를 완료했습니다.",
            ReportResponse.of(report)
        );
    }


    // MEMCTL02_REPORTS01 : 신고 목록 (관리자)
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RsData<ReportListResponse> getReports(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Report> reportPage = reportService.getReports(pageable);

        return new RsData<>(
"200-1",
            "신고 목록을 조회했습니다.",
            new ReportListResponse(
                reportPage.map(ReportResponse::of).getContent(),
                reportPage.getTotalElements()
            )
        );
    }


    // MEMCTL02_REPORTS02 : 허위 신고 제거 (관리자)
    @PatchMapping("/{reportId}/deactivate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RsData<Void> rejectReport(@PathVariable Long reportId) {
        reportService.rejectReport(reportId);
        return new RsData<>("200-1", "해당 신고를 비활성화했습니다.");
    }


    // MEMCTL02_REPORTS03 : 신고 확정 처리 (관리자)
    @PatchMapping("/{reportId}/confirm")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RsData<Void> confirmReport(@PathVariable Long reportId) {
        reportService.confirmReport(reportId);
        return new RsData<>("200-1", "%d번 신고를 확정 처리했습니다.".formatted(reportId));
    }




    // 권한 테스트용도 (제거예정)
    record RoleChangeRequest(String role) {
    }
    @PatchMapping("/{memberId}/change-role")
    public RsData<Member> changeRole(
        @PathVariable Long memberId, @RequestBody RoleChangeRequest request) {

        Member member = memberService.findById(memberId)
            .orElseThrow(() -> new ServiceException(ErrorCode.MEMBER_NOT_FOUND));

        try {
            member.setRole(MemberRole.valueOf(request.role));
            memberRepository.save(member);
            return new RsData<>("200-1", "권한을 변경했습니다.", member);

        } catch (IllegalArgumentException e) {
            throw new ServiceException(ErrorCode.INVALID_ROLE);
        }
    }

}
