package com.ssafy.foody.report.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportComment {
    private int id;
    private int reportId;
    private String author; // user_id
    private String comment;
    private LocalDateTime createdAt;
}
