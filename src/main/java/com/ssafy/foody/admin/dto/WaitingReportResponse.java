package com.ssafy.foody.admin.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WaitingReportResponse {
	private int id;
	private String userId;
	private LocalDateTime createdAt;
	private String userName;
	private Double totalKcal;
	private Double totalCarb;
	private Double totalProtein;
	private Double totalFat;
	private Double totalSugar;
	private Double totalNatrium;
}
