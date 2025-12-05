package com.ssafy.foody.food.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFood {
    private int code;                 // PK (AUTO_INCREMENT)
    private String registUserId;          // FK
    
    private String name;            // 음식 이름
    private String standard;        // 기준량 (g 또는 ml)
    
    // 영양소 정보
    private Double kcal;
    private Double carb;
    private Double protein;
    private Double fat;
    private Double sugar;
    private Double natrium;    
    
    private LocalDateTime createdAt;
}