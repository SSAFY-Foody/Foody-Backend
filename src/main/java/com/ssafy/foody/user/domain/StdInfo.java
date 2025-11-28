package com.ssafy.foody.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StdInfo {
    private String userId;      // 유저 ID
    private double stdWeight;   // 표준 체중
    private double stdKcal;    // 표준 칼로리 (std_kcal)
    private double stdCarb;    // 표준 탄수화물 (g)
    private double stdProtein; // 표준 단백질 (g)
    private double stdFat;     // 표준 지방 (g)
    private double stdSugar;   // 표준 당 (g)
    private double stdNatrium; // 표준 나트륨 (g)
}
