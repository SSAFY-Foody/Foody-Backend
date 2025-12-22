package com.ssafy.foody.report.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 레포트 목록 조회용
 * 상세 정보는 제외하고 목록에 필요한 정보만 포함
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportListResponse {
    private int id;
    private Double score;
    private String comment;
    private Integer characterId;
    private Boolean isWaited;
    private Boolean isShared;

    // 하루 섭취 영양소 총합
    private Double totalKcal;
    private Double totalCarb;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugar;
    private Double totalNatrium;

    private LocalDateTime createdAt;
}
