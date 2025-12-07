package com.ssafy.foody.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.user.component.StdInfoCalculator;
import com.ssafy.foody.user.domain.StdInfo;
import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.dto.UserResponse;
import com.ssafy.foody.user.dto.UserUpdateRequest;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StdInfoCalculator stdInfoCalculator;

    // ID 중복 체크
    public boolean isIdDuplicate(String id) {
        return userMapper.existsById(id);
    }

    // 내 정보 조회 (ID로 찾기)
    public UserResponse findById(String userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
        UserResponse userResponse = new UserResponse(user);
        return userResponse;
    }
    
    @Override
    @Transactional
    public void updateUserInfo(String userId, UserUpdateRequest request) {
        // 기존 유저 정보 조회
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        // 유저 정보 업데이트 (값이 있는 것만)
        if (request.getName() != null) user.setName(request.getName());
        if (request.getAge() != null) user.setAge(request.getAge());
        if (request.getHeight() != null) user.setHeight(request.getHeight());
        if (request.getWeight() != null) user.setWeight(request.getWeight());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getActivityLevel() != null) user.setActivityLevel(request.getActivityLevel());
        if (request.getIsDiabetes() != null) user.setIsDiabetes(request.getIsDiabetes());
        
        // users 테이블 업데이트
        userMapper.updateUser(user);
        
        // GUEST -> USER 심사 로직
        // 현재 권한이 GUEST인지 확인
        if ("ROLE_GUEST".equals(user.getRole())) {
            log.info("GUEST 유저입니다. 기본 정보 검사 - userId: {}", userId);

            // 기본 정보가 모두 있는 지 검사
            if (checkBasicInfo(user)) {
                
                // 표준 정보(StdInfo)를 계산 및 DB 생성
                StdInfo stdInfo = stdInfoCalculator.calculate(user);
                if (stdInfo != null) {
                    userMapper.insertStdInfo(stdInfo);
                }
            	
                // 권한을 USER로 변경
                user.setRole("ROLE_USER");
                
                // 변경된 권한 DB에 저장
                userMapper.updateRole(user.getId(), "ROLE_USER"); 
                
                log.info("유저 권한 수정 완료 ROLE_GUEST -> ROLE_USER - userId: {}", userId);
            } else {
                log.info("아직 필수 정보가 부족하여 GUEST 상태 유지 - userId: {}", userId);
            }
        } else {
	        // 표준 영양소 재계산 및 DB 업데이트
	        StdInfo stdInfo = stdInfoCalculator.calculate(user);
	        if (stdInfo != null) {
	            userMapper.updateStdInfo(stdInfo);
	        }
        }
    }
    
    @Override
    @Transactional
    public void deleteUser(String userId) {
        // 존재하는 유저인지 확인
        if (!userMapper.existsById(userId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        // 유저 삭제
		userMapper.deleteUser(userId);
	}
    
    /**
     * 필수 정보(나이, 키, 몸무게, 성별, 활동량)가 모두 입력되었는지 확인
     */
    private boolean checkBasicInfo(User user) {
        // 신체 정보가 모두 있는지 확인 (0이나 null이 아니어야 함)
        boolean hasBasicInfo = 
                user.getAge() != null && user.getAge() > 0 &&
                user.getHeight() != null && user.getHeight() > 0 &&
                user.getWeight() != null && user.getWeight() > 0 &&
                user.getGender() != null && !user.getGender().isEmpty() &&
                user.getActivityLevel() != null && user.getActivityLevel() > 0;
        return hasBasicInfo;
    }
}