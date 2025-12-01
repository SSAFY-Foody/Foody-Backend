package com.ssafy.foody.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

	@Size(max = 50, message = "이름은 50자 내로 입력해주세요.")
    private String name;

    @Min(value = 1, message = "나이는 1살 이상이어야 합니다.")
    private Integer age;

    @Positive(message = "키는 양수여야 합니다.")
    private Double height;

    @Positive(message = "몸무게는 양수여야 합니다.")
    private Double weight;

    @Pattern(regexp = "^[MF]$", message = "성별은 'M' 또는 'F'여야 합니다.")
    private String gender;

    @Min(value = 1, message = "활동량 레벨은 최소 1이어야 합니다.")
    @Max(value = 5, message = "활동량 레벨은 최대 5이어야 합니다.")
    private Integer activityLevel;

    private Boolean isDiabetes; // 당뇨 여부 (Boolean 객체 사용)
}