package com.dut.jfix_be.dto.request;

import java.time.LocalDate;

import com.dut.jfix_be.enums.JlptLevel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LearningGoalRequest {
    @NotNull(message = "validation.target.level.required")
    private JlptLevel targetLevel;
    
    private String description;
    
    @NotNull(message = "validation.daily.minutes.required")
    @Min(value = 1, message = "validation.daily.minutes.min")
    private Integer dailyMinutes;
    
    @NotNull(message = "validation.daily.vocab.required")
    @Min(value = 1, message = "validation.daily.vocab.min")
    private Integer dailyVocabTarget;
    
    @NotNull(message = "validation.target.date.required")
    private LocalDate targetDate;
} 