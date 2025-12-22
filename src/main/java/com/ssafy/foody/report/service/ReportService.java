package com.ssafy.foody.report.service;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.foody.common.dto.PageResponse;
import com.ssafy.foody.report.dto.ReportComment;
import com.ssafy.foody.report.dto.ReportCommentRequest;
import com.ssafy.foody.report.dto.ReportListResponse;
import com.ssafy.foody.report.dto.ReportRequest;
import com.ssafy.foody.report.dto.ReportResponse;

public interface ReportService {

    // 레포트 생성
    void createReport(String userId, ReportRequest request);

    // 레포트 목록 조회
    PageResponse<ReportListResponse> getReportList(String userId, int page, LocalDate startDate, LocalDate endDate);

    // 상세 조회
    ReportResponse getReportDetail(String userId, int reportId);

    // 삭제
    void deleteReport(String userId, int reportId);

    // 공유 상태 변경
    void toggleShare(String userId, int reportId);

    // 공유된 레포트 목록 조회
    PageResponse<ReportResponse> getSharedReportList(int page);

    // 댓글 등록
    void addComment(String userId, ReportCommentRequest request);

    // 댓글 목록 조회
    List<ReportComment> getComments(int reportId);

    // 댓글 삭제
    void deleteComment(String userId, int commentId);
}
