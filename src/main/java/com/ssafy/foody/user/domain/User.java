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
    private int age;
    private String email;
    private double height;
    private double weight;
    private String gender;
    private Integer activityLevel;			// 활동 계수
    private boolean isDiabetes;			// 당뇨 여부
    private String role;
    private String provider;
    private String providerId;
    private LocalDateTime createdAt;
}
