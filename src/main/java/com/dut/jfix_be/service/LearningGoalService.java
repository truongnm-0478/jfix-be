package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.request.LearningGoalRequest;
import com.dut.jfix_be.dto.response.LearningGoalResponse;

public interface LearningGoalService {
    LearningGoalResponse createLearningGoal(LearningGoalRequest request);
    LearningGoalResponse getLearningGoal();
    boolean hasExistingGoal();
}
