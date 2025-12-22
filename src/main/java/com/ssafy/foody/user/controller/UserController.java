package com.ssafy.foody.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.user.dto.ChangePasswordRequest;
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
    @GetMapping
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername(); 
        log.debug("내 정보 조회 요청: {}", userId);

        // DB에서 최신 유저 정보 가져오기
        UserResponse response = userService.findById(userId);
        
        log.debug("정보 조회 성공: {}", response);

        return ResponseEntity.ok(response);
    }
    

    // 내 정보 수정
    @PatchMapping
    public ResponseEntity<String> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        String userId = userDetails.getUsername();
        log.info("유저 정보 수정 요청: {}", userId);
        
        userService.updateUserInfo(userId, request);
        
        return ResponseEntity.ok("정보 수정이 완료되었습니다.");
    }
    
    
    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        log.info("회원 탈퇴 요청: {}", userId);

        userService.deleteUser(userId);

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
    
    /**
     * 비밀번호 변경
     * POST /user/change-pw
     * Header: Authorization: Bearer {Token}
     */
    // 멱등성이 없기 때문에 PATCH가 아닌 POST
    @PostMapping("/change-pw")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        userService.changePassword(
                userDetails.getUsername(), // 토큰에서 추출한 ID
                request.getOldPassword(), 
                request.getNewPassword()
        );

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
