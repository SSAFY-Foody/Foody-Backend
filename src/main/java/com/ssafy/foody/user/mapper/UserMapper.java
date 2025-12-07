package com.ssafy.foody.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.user.domain.StdInfo;
import com.ssafy.foody.user.domain.User;

@Mapper
public interface UserMapper {

	// 회원가입
	void save(User user);

	// 회원정보 수정
	void updateUser(User user);
	
	// 유저 삭제
	void deleteUser(String id);

	// 소셜 ID로 조회
	User findByProviderId(String providerId);

	// id로 조회
	User findById(String id);

	// ID 중복 체크
	boolean existsById(String id);

	// 표준 영양소 정보 저장
	void insertStdInfo(StdInfo stdInfo);
	
	// 표준 영양소 정보 수정
	void updateStdInfo(StdInfo stdInfo);
	
	// 활동 계수 조회
	double getActivityValue(int level);
	
	// 유저 정보 + 표준 정보(StdInfo) 조인 조회
    User findUserWithStdInfo(String userId);
    
    // 권한 수정
    void updateRole(@Param("id") String id, @Param("role") String role);
}
