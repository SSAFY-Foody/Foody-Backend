package com.ssafy.foody.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.common.auth.jwt.JwtTokenProvider;
import com.ssafy.foody.user.component.StdInfoCalculator;
import com.ssafy.foody.user.domain.StdInfo;
import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.dto.LoginResponse;
import com.ssafy.foody.user.dto.SignupRequest;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserMapper userMapper;
    private final StdInfoCalculator stdInfoCalculator;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 일반 회원가입 로직
    @Transactional(rollbackFor = Exception.class)
    public void signup(SignupRequest request) {
        // 중복 체크
        if (userMapper.existsById(request.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = User.builder()
                .id(request.getId())
                .password(encodedPassword)
                .name(request.getName())
                .age(request.getAge())
                .email(request.getEmail())
                .height(request.getHeight())
                .weight(request.getWeight())
                .gender(request.getGender())
                .activityLevel(request.getActivityLevel())
                .isDiabetes(request.isDiabetes())
                
                .role("ROLE_USER")      // 기본 역할
                .provider(null)         // 일반 가입이므로 null
                .providerId(null)       // 일반 가입이므로 null
                .build();
        userMapper.save(newUser);
        
        // 표준 영양소 계산
        StdInfo stdInfo = stdInfoCalculator.calculate(newUser);
        if (stdInfo != null) {
            userMapper.insertStdInfo(stdInfo); // INSERT
        }
    }

    // 로그인
    public LoginResponse authenticate(String id, String rawPassword) {
        // 유저 조회
        User user = userMapper.findById(id);
        
        // 유저가 없음
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        // 비밀번호가 틀림
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
        
        // 토큰 발급
        String token = jwtTokenProvider.createToken(id);

        // 응답 객체 생성
        LoginResponse response = new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getRole()  
        );
        
        return response;
    }
}
