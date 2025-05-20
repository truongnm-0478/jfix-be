package com.dut.jfix_be.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardSummary();
    List<Map<String, Object>> getDailyActiveUsers(LocalDate startDate, LocalDate endDate);
    Map<String, Integer> getQuestionTypeDistribution();
    List<Map<String, Object>> getRecentUsers();
    List<Map<String, Object>> getTopStreakUsers(int limit);
    List<Map<String, Object>> getTopCardsStudiedUsers(int limit, LocalDate month);
} 