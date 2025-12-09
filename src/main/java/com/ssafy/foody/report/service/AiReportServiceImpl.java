package com.ssafy.foody.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ssafy.foody.report.dto.AiReportRequest;
import com.ssafy.foody.report.dto.AiReportResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiReportServiceImpl implements AiReportService {

    private final RestTemplate restTemplate;

    @Value("${ai.server.base.url}")
    private String aiServerBaseUrl;

    @Value("${ai.analysis.server.port}") // 분석 서버 포트 사용
    private String analysisServerPort;

    @Override
    public AiReportResponse analyzeMeal(AiReportRequest request) {
    	
        // URL 조합
        String url = String.format("%s:%s/api/analysis/report", aiServerBaseUrl, analysisServerPort);

        log.debug("AI 레포트 분석 요청: {}", url);

        try {
            // POST 요청
            ResponseEntity<AiReportResponse> response = restTemplate.postForEntity(
                    url,
                    request,
                    AiReportResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("AI 레포트 분석 중 오류 발생", e);
            return null; 
        }
    }
}