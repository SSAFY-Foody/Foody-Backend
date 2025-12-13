package com.ssafy.foody.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.admin.dto.ActivityLevelResponse;
import com.ssafy.foody.admin.dto.UpdateActivityLevelRequest;
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
	
	// 활동 레벨 설명 조회 
	String getActivityDesc(int level);
	
	// 유저 정보 + 표준 정보(StdInfo) 조인 조회
    User findUserWithStdInfo(String userId);
    
    // 권한 수정 (관리자(ADMIN 전용))
    int updateRole(@Param("id") String id, @Param("role") String role);
    
    // 활동 레벨 (Activity level) 수정 (관리자(ADMIN 전용))
    int updateActivityLevelByLevel(UpdateActivityLevelRequest request);
    
    // 활동 레벨 전체 조회 (관리자(ADMIN 전용))
    List<ActivityLevelResponse> findAllActivityLevels();
}
