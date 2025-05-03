package com.dut.jfix_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyLogRequest {
    private Integer id; // id của study log
    private Integer performance; // độ khó (0-3)
    // Dành cho thẻ dạng tự luận
    private String correctAnswer;
    private String feedbackProvided;
    private String userInput;
} 