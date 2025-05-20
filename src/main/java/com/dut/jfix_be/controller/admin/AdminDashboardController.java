package com.dut.jfix_be.controller.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/stats/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardSummary() {
        Map<String, Object> summaryStats = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.success(summaryStats));
    }

    @GetMapping("/stats/daily-active-users")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDailyActiveUsers(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Map<String, Object>> dailyActiveUsers = dashboardService.getDailyActiveUsers(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(dailyActiveUsers));
    }

    @GetMapping("/stats/question-type-distribution")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getQuestionTypeDistribution() {
        Map<String, Integer> distribution = dashboardService.getQuestionTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution));
    }

    @GetMapping("/stats/recent-users")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRecentUsers() {
        List<Map<String, Object>> recentUsers = dashboardService.getRecentUsers();
        return ResponseEntity.ok(ApiResponse.success(recentUsers));
    }
} 