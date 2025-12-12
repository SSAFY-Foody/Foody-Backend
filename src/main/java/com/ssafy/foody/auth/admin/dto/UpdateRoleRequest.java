package com.ssafy.foody.auth.admin.dto;

import lombok.Data;

@Data
public class UpdateRoleRequest {
	private String userId;
	private String role;
}
