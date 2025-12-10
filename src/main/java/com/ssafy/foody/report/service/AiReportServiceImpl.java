package com.ssafy.foody.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssafy.foody.report.dto.AiReportRequest;
import com.ssafy.foody.report.dto.AiReportResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiReportServiceImpl implements AiReportService {

	private final WebClient webClient;

    @Value("${ai.server.base.url}")
    private String aiServerBaseUrl;

    @Value("${ai.analysis.server.port}") // 분석 서버 포트
    private String analysisServerPort;

    @Override
    public AiReportResponse analyzeMeal(AiReportRequest request) {
    	
        String url = String.format("%s:%s/api/analysis/report", aiServerBaseUrl, analysisServerPort);
        log.debug("AI 레포트 분석 요청: {}", url);

        try {
            // WebClient 사용 (Chain 방식)
            return webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON) // JSON으로 보낸다고 명시
                    .bodyValue(request)                      // Body에 객체 담기 (자동으로 JSON 변환)
                    .retrieve()                              // 요청 전송
                    .bodyToMono(AiReportResponse.class)      // 1개의 응답(JSON)을 받아 객체로 매핑
                    .block();                                // 동기 처리를 위해 blocking
        } catch (Exception e) {
            log.error("AI 레포트 분석 중 오류 발생", e);
            return null; 
        }
    }
}