package com.ssafy.foody.report.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AiReportResponse {
    private Double score;        // 식단 점수
    private String comment;      // AI 코멘트
    private Integer characterId; // 추천 캐릭터 ID
}