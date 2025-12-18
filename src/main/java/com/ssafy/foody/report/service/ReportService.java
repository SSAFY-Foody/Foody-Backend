package com.ssafy.foody.report.service;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.foody.report.dto.ReportRequest;
import com.ssafy.foody.report.dto.ReportListResponse;
import com.ssafy.foody.report.dto.ReportResponse;

public interface ReportService {

    // 레포트 생성
    void createReport(String userId, ReportRequest request);

    // 레포트 목록 조회
    List<ReportListResponse> getReportList(String userId, int page, LocalDate startDate, LocalDate endDate);

    // 상세 조회
    ReportResponse getReportDetail(String userId, int reportId);

    // 삭제
    void deleteReport(String userId, int reportId);
}
