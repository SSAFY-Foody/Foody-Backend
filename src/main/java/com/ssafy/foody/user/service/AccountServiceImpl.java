package com.ssafy.foody.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.common.auth.jwt.JwtTokenProvider;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 일반 회원가입 로직
    @Transactional
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
        calculateAndSaveStdInfo(request);
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
        log.info("토큰 발급 완료: {}", id);

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
    
    // 표준 영양소 계산 (당뇨 여부 추가해야함)
    private void calculateAndSaveStdInfo(SignupRequest userInfo) {
        // 표준 체중 계산 (소수점 2자리 반올림)
        // 남성: (키 - 100) * 0.9
        // 여성: (키 - 100) * 0.85
        double height = userInfo.getHeight();
        double weight = userInfo.getWeight();
        int age = userInfo.getAge();
        
        double stdWeightTemp;
        if ("M".equals(userInfo.getGender())) {
            stdWeightTemp = (height - 100) * 0.9;
        } else {
            stdWeightTemp = (height - 100) * 0.85;
        }
        // 소수점 2자리 반올림
        double stdWeight = Math.round(stdWeightTemp * 100.0) / 100.0;

        // 활동 계수
        double activityValue = userMapper.getActivityValue(userInfo.getActivityLevel());

        // 표준 칼로리 섭취량 (std_kcal) 계산
        // 남성 = (표준 몸무게 * 10 + 본인 몸무게 * 6.25 - 본인 나이 * 5 + 5) * 활동 계수
        // 여성 = (표준 몸무게 * 10 + 본인 몸무게 * 6.25 - 본인 나이 * 5 - 161) * 활동 계수
        double baseVal = "M".equals(userInfo.getGender()) ? 5 : -161;
        
        double stdKcal = (stdWeight * 10 + height * 6.25 - age * 5 + baseVal) * activityValue;

        // 영양소 비율 계산 (g 단위)
        // 탄:단:지:당 = 6 : 1.4 : 2.2 : 0.4
        double stdCarb = stdKcal * 0.6 / 4.0;
        double stdProtein = stdKcal * 0.14 / 4.0;
        double stdFat = stdKcal * 0.22 / 9.0;
        double stdSugar = stdKcal * 0.04 / 4.0;
        double stdNatrium = 2.0; // 고정값

        log.debug("kcal: {}", Math.round(stdKcal * 100.0) / 100.0);
        
        // 객체 생성 및 DB 저장
        StdInfo stdInfo = StdInfo.builder()
                .userId(userInfo.getId())
                .stdWeight(stdWeight)
                .stdKcal(Math.round(stdKcal * 100.0) / 100.0)
                .stdCarb(Math.round(stdCarb * 100) / 100.0)  
                .stdProtein(Math.round(stdProtein * 100) / 100.0)
                .stdFat(Math.round(stdFat * 100) / 100.0)
                .stdSugar(Math.round(stdSugar * 100) / 100.0)
                .stdNatrium(stdNatrium)
                .build();

        userMapper.insertStdInfo(stdInfo);
    }
}
