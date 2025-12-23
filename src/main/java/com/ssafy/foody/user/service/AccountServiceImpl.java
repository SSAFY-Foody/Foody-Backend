package com.ssafy.foody.user.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.auth.jwt.JwtTokenProvider;
import com.ssafy.foody.email.service.EmailService;
import com.ssafy.foody.user.domain.StdInfo;
import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.dto.LoginResponse;
import com.ssafy.foody.user.dto.SignupRequest;
import com.ssafy.foody.user.helper.StdInfoCalculator;
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
    private final EmailService emailService; // 순환 참조 주의

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
                .isDiabetes(request.getIsDiabetes() != null ? request.getIsDiabetes() : false)
                
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
    @Transactional(readOnly = true)
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
    
    // ID 중복 체크
    @Transactional(readOnly = true)
    public boolean isIdDuplicate(String id) {
        return userMapper.existsById(id);
    }
    
    // [아이디 찾기] - 1단계 인증번호 발송
    @Override
    public void sendCodeForFindId(String email) {
    	// 이메일로 아이디 조회 (존재 여부 확인)
        String userId = userMapper.findIdByEmail(email);
        
        // 조회 결과가 null이면 없는 회원
        if (userId == null) {
            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");
        }

        // 존재하면 인증번호 발송
        emailService.sendVerificationCode(email);
    }

    // [아이디 찾기] - 2단계 인증 검증 -> 아이디 반환
    @Override
    public String verifyAndGetId(String email, String code) {
        // 인증번호 검증
        boolean isVerified = emailService.verifyCode(email, code);
        if (!isVerified) {
            throw new IllegalArgumentException("인증번호가 올바르지 않거나 만료되었습니다.");
        }
 
        // 인증 성공 시 아이디 조회해서 리턴
        return userMapper.findIdByEmail(email);
    }

    // [비밀번호 찾기] - 1단계 아이디+이메일 확인 후 인증번호 발송
    @Override
    public void sendCodeForFindPw(String id, String email) {
        // 아이디와 이메일이 일치하는 유저가 있는지 확인
        int count = userMapper.countByIdAndEmail(id, email);
        if (count == 0) {
            throw new IllegalArgumentException("일치하는 회원 정보가 없습니다.");
        }

        // 인증번호 발송
        emailService.sendVerificationCode(email);
    }

    // [비밀번호 찾기] - 2단계 인증번호 검증 -> 임시 비밀번호 변경 및 발송
    @Override
    @Transactional
    public void resetPassword(String id, String email, String code) {
        // 인증번호 검증
        boolean isVerified = emailService.verifyCode(email, code);
        if (!isVerified) {
            throw new IllegalArgumentException("인증번호가 올바르지 않거나 만료되었습니다.");
        }

        // 회원 정보 다시 확인
        int count = userMapper.countByIdAndEmail(id, email);
        if (count == 0) {
            throw new IllegalArgumentException("일치하는 회원 정보가 없습니다.");
        }

        // 임시 비밀번호 생성 (8자리)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // DB 업데이트 (암호화)
        userMapper.updatePassword(id, passwordEncoder.encode(tempPassword));

        // 이메일 전송 (새로운 비밀번호 안내)
        emailService.sendTemporaryPassword(email, tempPassword);
    }
}
