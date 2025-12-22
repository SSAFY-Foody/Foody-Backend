package com.ssafy.foody.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.email.dto.EmailRequest;
import com.ssafy.foody.email.service.EmailService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(name = "Email", description = "이메일 인증 API")
public class EmailController {

    private final EmailService emailService;

    /**
     * 인증 코드 발송 요청
     * POST /email/send
     * Body: { "email": "test@naver.com" }
     */
    @Operation(summary = "인증 코드 발송", description = "이메일로 인증 코드를 발송합니다.")
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    /**
     * 인증 코드 검증 요청
     * POST /email/verify
     * Body: { "email": "test@naver.com", "code": "123456" }
     */
    @Operation(summary = "인증 코드 검증", description = "발송된 이메일 인증 코드를 검증합니다.")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody EmailRequest request) {
        boolean isVerified = emailService.verifyCode(request.getEmail(), request.getCode());

        if (isVerified) {
            return ResponseEntity.ok("이메일 인증 성공!");
        } else {
            return ResponseEntity.status(400).body("인증 코드가 올바르지 않거나 만료되었습니다.");
        }
    }

    // 이메일 중복 체크
    // GET /email/check-email?email=test@naver.com
    @Operation(summary = "이메일 중복 확인", description = "이미 가입된 이메일인지 확인합니다.")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = emailService.isEmailDuplicate(email);
        return ResponseEntity.ok(exists); // true면 중복, false면 사용 가능
    }
}
