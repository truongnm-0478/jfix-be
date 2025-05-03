package com.dut.jfix_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.response.CardDifficultyDTO;
import com.dut.jfix_be.dto.response.ErrorImprovementDTO;
import com.dut.jfix_be.dto.response.ErrorRateDTO;
import com.dut.jfix_be.dto.response.StudyHeatmapDTO;
import com.dut.jfix_be.dto.response.TotalLearnedCardsDTO;
import com.dut.jfix_be.dto.response.UserAchievementDTO;
import com.dut.jfix_be.service.UserAchievementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class UserAchievementController {
    private final UserAchievementService userAchievementService;

    @GetMapping
    public ResponseEntity<List<UserAchievementDTO>> getUserAchievements() {
        return ResponseEntity.ok(userAchievementService.getCurrentUserAchievements());
    }

    @PostMapping("/calculate")
    public ResponseEntity<List<UserAchievementDTO>> calculateAndGetAchievements() {
        return ResponseEntity.ok(userAchievementService.calculateAndReturnCurrentUserAchievements());
    }

    /**
     * API lấy dữ liệu heatmap tần suất học của người dùng
     * Nghiệp vụ: Trả về danh sách ngày và số lượng thẻ đã học/ngày
     */
    @GetMapping("/study-heatmap")
    public ResponseEntity<List<StudyHeatmapDTO>> getStudyHeatmap() {
        return ResponseEntity.ok(userAchievementService.getStudyHeatmap());
    }

    /**
     * API lấy dữ liệu vẽ biểu đồ số lượng thẻ học theo thời gian
     * Nghiệp vụ: Trả về danh sách ngày và số lượng thẻ đã học/ngày
     */
    @GetMapping("/cards-over-time")
    public ResponseEntity<List<StudyHeatmapDTO>> getCardsOverTime() {
        return ResponseEntity.ok(userAchievementService.getCardsOverTime());
    }

    /**
     * API lấy dữ liệu vẽ biểu đồ tỉ lệ mắc lỗi (pie chart)
     * Nghiệp vụ: Trả về số lần đúng, số lần sai, tỉ lệ lỗi
     */
    @GetMapping("/error-rate")
    public ResponseEntity<ErrorRateDTO> getErrorRate() {
        return ResponseEntity.ok(userAchievementService.getErrorRate());
    }

    /**
     * API lấy dữ liệu vẽ biểu đồ tần suất cải thiện lỗi (line chart)
     * Nghiệp vụ: Trả về số lỗi mắc phải theo từng ngày
     */
    @GetMapping("/error-improvement")
    public ResponseEntity<List<ErrorImprovementDTO>> getErrorImprovement() {
        return ResponseEntity.ok(userAchievementService.getErrorImprovement());
    }

    /**
     * API thống kê tổng số thẻ đã học
     * Nghiệp vụ: Trả về tổng số thẻ đã học của user
     */
    @GetMapping("/total-learned-cards")
    public ResponseEntity<TotalLearnedCardsDTO> getTotalLearnedCards() {
        return ResponseEntity.ok(userAchievementService.getTotalLearnedCards());
    }

    /**
     * API lấy dữ liệu vẽ biểu đồ thống kê số lượng thẻ theo độ khó (bar chart)
     */
    @GetMapping("/cards-by-difficulty")
    public ResponseEntity<List<CardDifficultyDTO>> getCardsByDifficulty() {
        return ResponseEntity.ok(userAchievementService.getCardsByDifficulty());
    }
} 