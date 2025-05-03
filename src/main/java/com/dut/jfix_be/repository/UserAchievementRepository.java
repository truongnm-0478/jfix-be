package com.dut.jfix_be.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.UserAchievement;
import com.dut.jfix_be.enums.AchievementType;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {
    List<UserAchievement> findByUserId(Integer userId);
    Optional<UserAchievement> findByUserIdAndAchievementType(Integer userId, AchievementType achievementType);
} 