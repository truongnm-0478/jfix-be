package com.dut.jfix_be.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dut.jfix_be.entity.LearningGoal;
import com.dut.jfix_be.enums.JlptLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningGoalResponse {
    private Integer id;
    private Integer userId;
    private JlptLevel targetLevel;
    private String description;
    private Integer dailyMinutes;
    private Integer dailyVocabTarget;
    private LocalDate targetDate;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;

    public static LearningGoalResponse fromLearningGoal(LearningGoal learningGoal) {
        return LearningGoalResponse.builder()
                .id(learningGoal.getId())
                .userId(learningGoal.getUserId())
                .targetLevel(learningGoal.getTargetLevel())
                .description(learningGoal.getDescription())
                .dailyMinutes(learningGoal.getDailyMinutes())
                .dailyVocabTarget(learningGoal.getDailyVocabTarget())
                .targetDate(learningGoal.getTargetDate())
                .createDate(learningGoal.getCreateDate())
                .createBy(learningGoal.getCreateBy())
                .updateDate(learningGoal.getUpdateDate())
                .updateBy(learningGoal.getUpdateBy())
                .build();
    }
} 