package com.ssafy.foody.admin.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateWaitingReportRequest {
	
	@NotNull(message = "Report ID는 필수 값입니다.")
	private Integer id;
	
	@NotNull(message = "score는 필수 값입니다")
	@DecimalMin(value = "0.0", inclusive = true, message = "score는 0 이상이어야 합니다")
	@DecimalMax(value = "100.0", inclusive = true, message = "score는 100 이하여야 합니다")
	private double score;
	
	@PositiveOrZero(message = "캐릭터 ID 는 0 이상이여야 합니다")
	private Integer characterId;
	
	@NotBlank(message = "코멘트 입력은 필수입니다.")
	private String comment;
}
