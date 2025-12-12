package com.ssafy.foody.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminUserMapper {
	int updateUserRole(@Param("userId") String userId);
}
