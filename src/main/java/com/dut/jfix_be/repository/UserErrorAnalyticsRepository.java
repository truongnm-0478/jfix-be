package com.dut.jfix_be.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.UserErrorAnalytics;

@Repository
public interface UserErrorAnalyticsRepository extends JpaRepository<UserErrorAnalytics, Integer> {
    Optional<UserErrorAnalytics> findByUserIdAndCardId(Integer userId, Integer cardId);
    void deleteAllByCardId(Integer cardId);
} 