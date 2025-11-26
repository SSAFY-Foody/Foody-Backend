package com.ssafy.foody.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.dto.UserResponse;
import com.ssafy.foody.user.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

//    @GetMapping("/mypage")
//    public ResponseEntity<UserResponse> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
//        // @AuthenticationPrincipal: 토큰에서 파싱한 유저 정보를 바로 꽂아줍니다.
//        String userId = userDetails.getUsername(); 
//        log.info("내 정보 조회 요청: {}", userId);
//
//        // DB에서 최신 유저 정보 가져오기
//        User user = userService.findById(userId);
//        
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // 도메인(User) -> 응답DTO(UserResponse) 변환
//        // (UserResponse 생성자는 직접 만드셔야 합니다)
//        UserResponse response = new UserResponse(user);
//
//        return ResponseEntity.ok(response);
//    }
}
