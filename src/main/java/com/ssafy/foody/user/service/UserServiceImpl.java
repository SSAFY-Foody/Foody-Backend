package com.ssafy.foody.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.dto.UserResponse;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

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
    
    // 추후 추가될 기능들:
    // public void updateUserProfile(UserUpdateDto dto) { ... }
    // public void deleteUser(String id) { ... }
}