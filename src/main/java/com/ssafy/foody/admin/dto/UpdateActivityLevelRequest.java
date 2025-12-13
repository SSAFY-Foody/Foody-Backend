package com.ssafy.foody.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateActivityLevelRequest {
	@NotNull
	private Integer level; 
	
	@PositiveOrZero(message = "value는 0 이상이여야 합니다.")
	private double value;
	
	private String description;
}
