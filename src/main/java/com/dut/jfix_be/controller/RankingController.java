package com.dut.jfix_be.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final DashboardService dashboardService;

    @GetMapping("/top-streak-users")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopStreakUsers(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        List<Map<String, Object>> topStreakUsers = dashboardService.getTopStreakUsers(limit);
        return ResponseEntity.ok(ApiResponse.success(topStreakUsers));
    }

    @GetMapping("/top-cards-users")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopCardsStudiedUsers(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "month", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        List<Map<String, Object>> topCardsStudiedUsers = dashboardService.getTopCardsStudiedUsers(limit, month);
        return ResponseEntity.ok(ApiResponse.success(topCardsStudiedUsers));
    }
} 