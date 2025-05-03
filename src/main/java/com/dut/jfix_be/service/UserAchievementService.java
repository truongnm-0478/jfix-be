package com.dut.jfix_be.service;

import java.util.List;

import com.dut.jfix_be.dto.response.CardDifficultyDTO;
import com.dut.jfix_be.dto.response.ErrorImprovementDTO;
import com.dut.jfix_be.dto.response.ErrorRateDTO;
import com.dut.jfix_be.dto.response.StudyHeatmapDTO;
import com.dut.jfix_be.dto.response.TotalLearnedCardsDTO;
import com.dut.jfix_be.dto.response.UserAchievementDTO;

public interface UserAchievementService {
    List<UserAchievementDTO> getUserAchievements(Integer userId);
    void calculateAndSaveAchievements(Integer userId);
    List<UserAchievementDTO> calculateAndReturnAchievements(Integer userId);
    List<UserAchievementDTO> getCurrentUserAchievements();
    void calculateAndSaveCurrentUserAchievements();
    List<UserAchievementDTO> calculateAndReturnCurrentUserAchievements();
    List<StudyHeatmapDTO> getStudyHeatmap();
    List<StudyHeatmapDTO> getCardsOverTime();
    ErrorRateDTO getErrorRate();
    List<ErrorImprovementDTO> getErrorImprovement();
    TotalLearnedCardsDTO getTotalLearnedCards();
    List<CardDifficultyDTO> getCardsByDifficulty();
}

