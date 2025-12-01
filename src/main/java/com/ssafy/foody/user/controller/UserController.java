package com.ssafy.foody.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.user.dto.UserResponse;
import com.ssafy.foody.user.dto.UserUpdateRequest;
import com.ssafy.foody.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/info")
    public ResponseEntity<UserResponse> getInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername(); 
        log.info("내 정보 조회 요청: {}", userId);

        // DB에서 최신 유저 정보 가져오기
        UserResponse response = userService.findById(userId);
        
        log.debug("정보 조회 성공: {}", response);

        return ResponseEntity.ok(response);
    }
    

    // 내 정보 수정
    @PatchMapping("/info")
    public ResponseEntity<String> updateInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        String userId = userDetails.getUsername();
        log.info("유저 정보 수정 요청: {}", userId);
        
        userService.updateUserInfo(userId, request);
        
        return ResponseEntity.ok("정보 수정이 완료되었습니다.");
    }
}
