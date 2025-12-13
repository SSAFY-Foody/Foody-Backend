package com.ssafy.foody.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityLevelResponse {
	private int level;
	private double value;
	private String description;
}
