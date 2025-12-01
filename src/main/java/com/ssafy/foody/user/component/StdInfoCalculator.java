package com.ssafy.foody.user.component;

import org.springframework.stereotype.Component;

import com.ssafy.foody.user.domain.StdInfo;
import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StdInfoCalculator {
	
	private final UserMapper userMapper;
	
	// 표준 영양소 계산
	public StdInfo calculate(User user) {
        // 표준 체중 계산 (소수점 2자리 반올림)
        // 남성: (키 - 100) * 0.9
        // 여성: (키 - 100) * 0.85
        double height = user.getHeight();
        double weight = user.getWeight();
        int age = user.getAge();
        
        double stdWeightTemp;
        if ("M".equals(user.getGender())) {
            stdWeightTemp = (height - 100) * 0.9;
        } else {
            stdWeightTemp = (height - 100) * 0.85;
        }
        // 소수점 2자리 반올림
        double stdWeight = Math.round(stdWeightTemp * 100.0) / 100.0;

        // 활동 계수
        double activityValue = userMapper.getActivityValue(user.getActivityLevel());

        // 표준 칼로리 섭취량 (std_kcal) 계산
        // 남성 = (표준 몸무게 * 10 + 본인 몸무게 * 6.25 - 본인 나이 * 5 + 5) * 활동 계수
        // 여성 = (표준 몸무게 * 10 + 본인 몸무게 * 6.25 - 본인 나이 * 5 - 161) * 활동 계수
        double baseVal = "M".equals(user.getGender()) ? 5 : -161;
        
        double stdKcal = (stdWeight * 10 + height * 6.25 - age * 5 + baseVal) * activityValue;

        // 영양소 비율 계산 (g 단위)
        // 탄:단:지:당 = 6 : 1.4 : 2.2 : 0.4
        double stdCarb = stdKcal * 0.6 / 4.0;
        double stdProtein = stdKcal * 0.14 / 4.0;
        double stdFat = stdKcal * 0.22 / 9.0;
        double stdSugar = stdKcal * 0.04 / 4.0;
        double stdNatrium = 2.0; // 고정값

        log.debug("kcal: {}", Math.round(stdKcal * 100.0) / 100.0);
        
        return StdInfo.builder()
                .userId(user.getId())
                .stdWeight(stdWeight)
                .stdKcal(Math.round(stdKcal * 100.0) / 100.0)
                .stdCarb(Math.round(stdCarb * 100) / 100.0)  
                .stdProtein(Math.round(stdProtein * 100) / 100.0)
                .stdFat(Math.round(stdFat * 100) / 100.0)
                .stdSugar(Math.round(stdSugar * 100) / 100.0)
                .stdNatrium(stdNatrium)
                .build();
    }
}
