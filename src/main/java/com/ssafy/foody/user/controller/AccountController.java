package com.ssafy.foody.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.admin.dto.ActivityLevelResponse;
import com.ssafy.foody.admin.service.AdminService;
import com.ssafy.foody.user.dto.FindAccountRequest;
import com.ssafy.foody.user.dto.LoginRequest;
import com.ssafy.foody.user.dto.LoginResponse;
import com.ssafy.foody.user.dto.SignupRequest;
import com.ssafy.foody.user.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "계정 및 인증 API")
public class AccountController {

    private final AccountService accountService;
    private final AdminService adminService;

    // 회원가입
    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        log.info("회원가입 요청: {}", request.getId());
        log.debug("회원가입 request: {}", request);
        accountService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 로그인 (JWT 발급)
    @Operation(summary = "로그인", description = "ID와 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("로그인 요청: {}", request.getId());

        // ID/PW 검증 후 인증된 유저 정보 받기
        LoginResponse loginResponse = accountService.authenticate(request.getId(), request.getPassword());

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * 로그아웃
     * 리프레쉬 토큰이 없으므로, 서버에선 할 일 X (리프레쉬 토큰 도입 시 수정 필요)
     */
    @Operation(summary = "로그아웃", description = "로그아웃 처리를 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 아이디 중복 체크
    @Operation(summary = "아이디 중복 확인", description = "아이디 중복 여부를 확인합니다.")
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestParam String id) {
        boolean exists = accountService.isIdDuplicate(id);
        return ResponseEntity.ok(exists);
    }

    /**
     * 활동 레벨 전체 조회 (Public - 회원가입용)
     * 인증 없이 조회 가능
     */
    @Operation(summary = "활동 레벨 목록 조회", description = "회원가입 시 선택할 활동 레벨 목록을 조회합니다.")
    @GetMapping("/activitylevels")
    public ResponseEntity<List<ActivityLevelResponse>> getActivityLevels() {
        List<ActivityLevelResponse> list = adminService.findAllActivityLevels();
        return ResponseEntity.ok(list);
    }

    // 아이디 찾기 - 인증번호 발송 요청
    @Operation(summary = "아이디 찾기 - 인증번호 발송", description = "아이디 찾기를 위해 이메일로 인증번호를 발송합니다.")
    @PostMapping("/find-id/send")
    public ResponseEntity<String> sendCodeForId(@RequestBody @Valid FindAccountRequest.FindIdSend request) {
        // 이름 없이 이메일만 보냄
        accountService.sendCodeForFindId(request.getEmail());
        return ResponseEntity.ok("인증번호가 이메일로 발송되었습니다.");
    }

    // 아이디 찾기 - 인증번호 검증 및 아이디 반환
    @Operation(summary = "아이디 찾기 - 인증 및 확인", description = "인증번호를 검증하고 해당 이메일로 가입된 아이디를 반환합니다.")
    @PostMapping("/find-id/verify")
    public ResponseEntity<String> findIdVerify(@RequestBody @Valid FindAccountRequest.FindIdVerify request) {
        String userId = accountService.verifyAndGetId(request.getEmail(), request.getCode());
        return ResponseEntity.ok(userId); // "testUser123" 반환
    }

    // [비밀번호 찾기] 인증번호 발송 요청
    @Operation(summary = "비밀번호 찾기 - 인증번호 발송", description = "비밀번호 찾기를 위해 아이디와 이메일로 인증번호를 발송합니다.")
    @PostMapping("/find-pw/send")
    public ResponseEntity<String> sendCodeForPw(@RequestBody @Valid FindAccountRequest.FindPwSend request) {
        accountService.sendCodeForFindPw(request.getId(), request.getEmail());
        return ResponseEntity.ok("인증번호가 이메일로 발송되었습니다.");
    }

    // [비밀번호 찾기] 인증번호 검증 및 임시 비밀번호 발급
    @Operation(summary = "비밀번호 찾기 - 인증 및 초기화", description = "인증번호 검증 후 임시 비밀번호를 이메일로 발송합니다.")
    @PostMapping("/find-pw/verify")
    public ResponseEntity<String> findPwVerify(@RequestBody @Valid FindAccountRequest.FindPwVerify request) {
        accountService.resetPassword(request.getId(), request.getEmail(), request.getCode());
        return ResponseEntity.ok("인증에 성공하여 이메일로 임시 비밀번호가 발송되었습니다.");
    }

}
