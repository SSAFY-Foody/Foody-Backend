package com.ssafy.foody.report.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.common.dto.PageResponse;
import com.ssafy.foody.report.dto.ReportComment;
import com.ssafy.foody.report.dto.ReportCommentRequest;
import com.ssafy.foody.report.dto.ReportListResponse;
import com.ssafy.foody.report.dto.ReportRequest;
import com.ssafy.foody.report.dto.ReportResponse;
import com.ssafy.foody.report.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Tag(name = "Report", description = "식단 분석 및 식단 공유 API")
public class ReportController {

    private final ReportService reportService;

    // 레포트 생성
    @Operation(summary = "식단 분석 레포트 생성", description = "입력된 식단 정보를 바탕으로 분석 레포트를 생성합니다.")
    @PostMapping
    public ResponseEntity<String> createReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ReportRequest request) {
        String userId = userDetails.getUsername();
        log.info("레포트 생성 요청 - User: {}", userId);

        // 서비스 호출
        reportService.createReport(userId, request);

        return ResponseEntity.ok("레포트가 성공적으로 생성되었습니다.");
    }

    /**
     * 레포트 목록 조회
     * GET /report?page=1&startDate=2025-01-01&endDate=2025-01-31 (기간 조회)
     * GET /report?page=1 (전체 조회)
     * page는 생략 가능 (1이 기본)
     */
    @Operation(summary = "내 레포트 목록 조회", description = "나의 식단 분석 레포트 목록을 조회합니다. 기간별 조회가 가능합니다.")
    @GetMapping
    public ResponseEntity<PageResponse<ReportListResponse>> getReportList(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        String userId = userDetails.getUsername();

        PageResponse<ReportListResponse> reports = reportService.getReportList(userId, page, startDate, endDate);

        return ResponseEntity.ok(reports);
    }

    /**
     * 레포트 상세 조회
     * GET /report/{reportId}
     */
    @Operation(summary = "레포트 상세 조회", description = "특정 레포트의 상세 분석 결과를 조회합니다.")
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int reportId) {
        String userId = userDetails.getUsername();

        ReportResponse report = reportService.getReportDetail(userId, reportId);

        return ResponseEntity.ok(report);
    }

    /**
     * 레포트 삭제
     * DELETE /report/{reportId}
     */
    @Operation(summary = "레포트 삭제", description = "특정 레포트를 삭제합니다.")
    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int reportId) {
        String userId = userDetails.getUsername();

        reportService.deleteReport(userId, reportId);

        return ResponseEntity.ok("레포트가 삭제되었습니다.");
    }

    /**
     * 레포트 공유 토글
     * PATCH /report/{id}/share
     */
    @Operation(summary = "레포트 공유 상태 변경", description = "레포트의 공유 여부(공개/비공개)를 변경합니다.")
    @PatchMapping("/{id}/share")
    public ResponseEntity<String> toggleShare(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int id) {
        String userId = userDetails.getUsername();
        reportService.toggleShare(userId, id);
        return ResponseEntity.ok("공유 상태가 변경되었습니다.");
    }

    /**
     * 공유된 레포트 목록 (커뮤니티)
     * GET /report/shared?page=1
     */
    @Operation(summary = "공유된 레포트 목록 조회", description = "커뮤니티에 공유된 다른 유저들의 레포트 목록을 조회합니다.")
    @GetMapping("/shared")
    public ResponseEntity<PageResponse<ReportResponse>> getSharedReportList(
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(reportService.getSharedReportList(page));
    }

    /**
     * 댓글 작성
     * POST /report/comment
     */
    @Operation(summary = "댓글 작성", description = "공유된 레포트에 댓글을 작성합니다.")
    @PostMapping("/comment")
    public ResponseEntity<String> addComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ReportCommentRequest request) {
        String userId = userDetails.getUsername();
        reportService.addComment(userId, request);
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    /**
     * 댓글 조회
     * GET /report/{id}/comment
     */
    @Operation(summary = "댓글 목록 조회", description = "특정 레포트에 작성된 댓글 목록을 조회합니다.")
    @GetMapping("/{id}/comment")
    public ResponseEntity<List<ReportComment>> getComments(
            @PathVariable int id) {
        return ResponseEntity.ok(reportService.getComments(id));
    }

    /**
     * 댓글 삭제
     * DELETE /report/comment/{id}
     */
    @Operation(summary = "댓글 삭제", description = "내가 작성한 댓글을 삭제합니다.")
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int id) {
        String userId = userDetails.getUsername();
        reportService.deleteComment(userId, id);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

}
