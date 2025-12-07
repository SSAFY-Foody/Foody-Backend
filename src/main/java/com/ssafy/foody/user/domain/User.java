package com.ssafy.foody.user.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String password;
    private String name;
    private Integer age;
    private String email;
    private Double height;
    private Double weight;
    private String gender;
    private Integer activityLevel;			// 활동 계수
    private Boolean isDiabetes;			// 당뇨 여부
    private String role;
    private String provider;
    private String providerId;
    private LocalDateTime createdAt;
    
    StdInfo stdInfo; // 표준 정보
}
