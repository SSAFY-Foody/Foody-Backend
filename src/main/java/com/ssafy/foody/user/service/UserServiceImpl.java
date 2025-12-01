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
    public UserResponse findById(String id) {
        User user = userMapper.findById(id);
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

        // 표준 영양소 재계산 및 DB 업데이트
        StdInfo stdInfo = stdInfoCalculator.calculate(user);
        if (stdInfo != null) {
            userMapper.updateStdInfo(stdInfo);
        }
    }
    // public void deleteUser(String id) { ... }
}