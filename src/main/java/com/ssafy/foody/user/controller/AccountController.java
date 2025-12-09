package com.ssafy.foody.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.user.dto.LoginRequest;
import com.ssafy.foody.user.dto.LoginResponse;
import com.ssafy.foody.user.dto.SignupRequest;
import com.ssafy.foody.user.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    
    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        log.info("회원가입 요청: {}", request.getId());
        log.debug("회원가입 request: {}", request);
        accountService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 로그인 (JWT 발급)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("로그인 요청: {}", request.getId());

        // ID/PW 검증 후 인증된 유저 정보 받기
        LoginResponse loginResponse = accountService.authenticate(request.getId(), request.getPassword());

        return ResponseEntity.ok(loginResponse);
    }

    // 아이디 중복 체크
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestParam String id) {
        boolean exists = accountService.isIdDuplicate(id);
        return ResponseEntity.ok(exists); 
    }
    
    /**
     * 로그아웃
     * 리프레쉬 토큰이 없으므로, 서버에선 할 일 X (리프레쉬 토큰 도입 시 수정 필요)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
