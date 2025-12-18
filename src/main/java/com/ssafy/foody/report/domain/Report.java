package com.ssafy.foody.report.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private int id; // PK
    private String userId; // FK

    // 분석 정보
    private Double score; // 점수
    private String comment; // 코멘트
    private Integer characterId; // 캐릭터 ID

    private boolean isWaited; // 전문가 분석 요청 여부

    // 하루 섭취 영양소 총합
    private Double totalKcal;
    private Double totalCarb;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugar;
    private Double totalNatrium;

    // 생성 당시 유저 신체 정보
    private Integer userAge;
    private Double userHeight;
    private Double userWeight;
    private String userGender;
    private Integer userActivityLevel;

    // 생성 당시 유저 표준 정보
    private Double userStdWeight;
    private Double userStdKcal;
    private Double userStdCarb;
    private Double userStdProtein;
    private Double userStdFat;
    private Double userStdSugar;
    private Double userStdNatrium;

    private LocalDateTime createdAt;

    // 1:N 관계, 끼니별 정보
    private List<Meal> meals;

    // 기본형 Getter/Setter 직접 선언 (네이밍 불일치 해결) - 혹은 Boolean 써도 됨
    public boolean getIsWaited() {
        return isWaited;
    }

    public void setIsWaited(boolean isWaited) {
        this.isWaited = isWaited;
    }
}