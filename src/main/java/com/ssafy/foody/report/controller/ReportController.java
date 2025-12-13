package com.ssafy.foody.report.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.report.domain.Report;
import com.ssafy.foody.report.dto.ReportRequest;
import com.ssafy.foody.report.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 레포트 생성
    @PostMapping
    public ResponseEntity<String> createReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ReportRequest request         // 식단 데이터
    ) {
        String userId = userDetails.getUsername();
        log.info("리포트 생성 요청 - User: {}", userId);

        // 서비스 호출
        reportService.createReport(userId, request);
        
        return ResponseEntity.ok("리포트가 성공적으로 생성되었습니다.");
    }
    
    /**
     * 레포트 목록 조회
     * GET /report?page=1&startDate=2025-01-01&endDate=2025-01-31 (기간 조회)
     * GET /report?page=1 (전체 조회)
     * page는 생략 가능 (1이 기본)
     */
    @GetMapping
    public ResponseEntity<List<Report>> getReportList(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        String userId = userDetails.getUsername();
        
        List<Report> reports = reportService.getReportList(userId, page, startDate, endDate);
        
        return ResponseEntity.ok(reports);
    }
    
    /**
     * 레포트 상세 조회
     * GET /report/{reportId}
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<Report> getReportDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int reportId
    ) {
        String userId = userDetails.getUsername();
        
        Report report = reportService.getReportDetail(userId, reportId);
        
        return ResponseEntity.ok(report);
    }
    
    /**
     * 레포트 삭제
     * DELETE /report/{reportId}
     */
    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int reportId
    ) {
        String userId = userDetails.getUsername();
        
        reportService.deleteReport(userId, reportId);
        
        return ResponseEntity.ok("레포트가 삭제되었습니다.");
    }
    
}
