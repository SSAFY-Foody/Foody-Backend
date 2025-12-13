package com.ssafy.foody.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {
	@NotBlank(message = "Id 입력은 필수입니다")
	private String userId;
	
	@NotBlank(message = "권한 입력은 필수입니다.")
	private String role;
}
