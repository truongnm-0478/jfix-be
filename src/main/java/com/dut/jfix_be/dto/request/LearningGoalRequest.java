package com.dut.jfix_be.dto.request;

import java.time.LocalDate;

import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.validation.LearningGoalFieldsValidation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@LearningGoalFieldsValidation
public class LearningGoalRequest {
    @NotNull(message = "validation.target.level.required")
    private JlptLevel targetLevel;
    
    private String description;
    
    @Min(value = 1, message = "validation.daily.minutes.min")
    private Integer dailyMinutes;
    
    @Min(value = 1, message = "validation.daily.vocab.min")
    private Integer dailyVocabTarget;
    
    private LocalDate targetDate;
} 