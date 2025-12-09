package com.ssafy.foody.report.service;

import com.ssafy.foody.report.dto.AiReportRequest;
import com.ssafy.foody.report.dto.AiReportResponse;

public interface AiReportService {
    AiReportResponse analyzeMeal(AiReportRequest request); // 식단 분석
}