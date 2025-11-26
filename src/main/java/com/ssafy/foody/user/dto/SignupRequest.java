package com.ssafy.foody.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다.")
	private String id; // 사용자 ID (PK)

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	private String password; // 비밀번호

	@NotBlank(message = "이름은 필수입니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z]{1,10}$", message = "이름은 특수문자 제외 1~10자로 입력해주세요.")
	private String name;

	@Min(value = 1, message = "나이는 1살 이상이어야 합니다.")
	private int age;

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	@Positive(message = "키는 양수여야 합니다.")
	private double height;

	@Positive(message = "몸무게는 양수여야 합니다.")
	private double weight;

	@Pattern(regexp = "^[MF]$", message = "성별은 'M' 또는 'F'여야 합니다.")
	private String gender;

	@Min(value = 1, message = "활동량 레벨은 최소 1이어야 합니다.")
	@Max(value = 5, message = "활동량 레벨은 최대 5이어야 합니다.")
	private int activityLevel;

	private boolean isDiabetes; // 당뇨 여부 (boolean은 true/false라 유효성 검사 불필요)
}
