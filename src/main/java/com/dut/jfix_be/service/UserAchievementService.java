package com.dut.jfix_be.service;

import java.util.List;

import com.dut.jfix_be.dto.response.CardDifficultyResponse;
import com.dut.jfix_be.dto.response.ErrorImprovementResponse;
import com.dut.jfix_be.dto.response.ErrorRateResponse;
import com.dut.jfix_be.dto.response.StudyHeatmapResponse;
import com.dut.jfix_be.dto.response.TotalLearnedCardsResponse;
import com.dut.jfix_be.dto.response.UserAchievementResponse;

public interface UserAchievementService {
    List<UserAchievementResponse> getUserAchievements(Integer userId);
    void calculateAndSaveAchievements(Integer userId);
    List<UserAchievementResponse> calculateAndReturnAchievements(Integer userId);
    List<UserAchievementResponse> getCurrentUserAchievements();
    void calculateAndSaveCurrentUserAchievements();
    List<UserAchievementResponse> calculateAndReturnCurrentUserAchievements();
    List<StudyHeatmapResponse> getStudyHeatmap();
    List<StudyHeatmapResponse> getCardsOverTime();
    ErrorRateResponse getErrorRate();
    List<ErrorImprovementResponse> getErrorImprovement();
    TotalLearnedCardsResponse getTotalLearnedCards();
    List<CardDifficultyResponse> getCardsByDifficulty();
}

