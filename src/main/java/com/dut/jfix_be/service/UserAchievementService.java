package com.dut.jfix_be.service;

import java.util.List;

import com.dut.jfix_be.dto.response.UserAchievementDTO;

public interface UserAchievementService {
    List<UserAchievementDTO> getUserAchievements(Integer userId);
    void calculateAndSaveAchievements(Integer userId);
    List<UserAchievementDTO> calculateAndReturnAchievements(Integer userId);
    List<UserAchievementDTO> getCurrentUserAchievements();
    void calculateAndSaveCurrentUserAchievements();
    List<UserAchievementDTO> calculateAndReturnCurrentUserAchievements();
}

