package com.ssafy.foody.report.service;

import com.ssafy.foody.report.dto.ReportRequest;

public interface ReportService {

	// 레포트 생성
	void createReport(String userId, ReportRequest request);
}
