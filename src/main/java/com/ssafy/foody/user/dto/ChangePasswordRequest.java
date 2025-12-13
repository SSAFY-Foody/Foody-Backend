package com.ssafy.foody.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
	@NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String oldPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String newPassword;
}
