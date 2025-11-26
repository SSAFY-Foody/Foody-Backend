package com.ssafy.foody.user.dto;

import com.ssafy.foody.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private double height;  
    private double weight;
    private String gender;
    private int activityLevel;
    private String role;          // "ROLE_USER"

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.gender = user.getGender();
        this.activityLevel = user.getActivityLevel();
        this.role = user.getRole();
    }
}
