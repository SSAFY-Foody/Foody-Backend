package com.ssafy.foody.admin.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportWaitingResponse {
	private int id;
	private String userId;
	private LocalDateTime createdAt;
}
