package com.ssafy.foody.report.service;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.foody.report.domain.Report;
import com.ssafy.foody.report.dto.ReportRequest;

public interface ReportService {

	// 레포트 생성
	void createReport(String userId, ReportRequest request);
	
	// 레포트 목록 조회
    List<Report> getReportList(String userId, int page, LocalDate startDate, LocalDate endDate);
    
    // 상세 조회
    Report getReportDetail(String userId, int reportId);
    
    // 삭제
    void deleteReport(String userId, int reportId);
}
