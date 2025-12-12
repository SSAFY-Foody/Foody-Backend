package com.ssafy.foody.admin.dto;

import lombok.Data;

@Data
public class UpdateRoleRequest {
	private String userId;
	private String role;
}
