package com.ssafy.foody.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
