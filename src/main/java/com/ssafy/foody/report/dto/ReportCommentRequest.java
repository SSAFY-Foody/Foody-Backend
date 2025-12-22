package com.ssafy.foody.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCommentRequest {
    private int reportId;
    
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String comment;
}
