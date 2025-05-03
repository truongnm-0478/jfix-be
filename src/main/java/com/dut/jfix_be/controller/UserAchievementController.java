package com.dut.jfix_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
} 