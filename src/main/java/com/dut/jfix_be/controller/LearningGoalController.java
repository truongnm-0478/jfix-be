package com.dut.jfix_be.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.request.LearningGoalRequest;
import com.dut.jfix_be.dto.response.LearningGoalResponse;
import com.dut.jfix_be.service.LearningGoalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/learning-goals")
public class LearningGoalController {

    private final LearningGoalService learningGoalService;
    
    public LearningGoalController(LearningGoalService learningGoalService) {
        this.learningGoalService = learningGoalService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LearningGoalResponse>> createLearningGoal(
            @Valid @RequestBody LearningGoalRequest request) {
        LearningGoalResponse response = learningGoalService.createLearningGoal(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<LearningGoalResponse>> getLearningGoal() {
        LearningGoalResponse response = learningGoalService.getLearningGoal();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkExistingGoal() {
        boolean hasGoal = learningGoalService.hasExistingGoal();
        return ResponseEntity.ok(ApiResponse.success(hasGoal));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<LearningGoalResponse>> updateLearningGoal(
            @Valid @RequestBody LearningGoalRequest request) {
        LearningGoalResponse response = learningGoalService.updateLearningGoal(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
} 