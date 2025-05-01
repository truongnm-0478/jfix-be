package com.dut.jfix_be.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.response.ReviewDeckResponse;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.Skill;
import com.dut.jfix_be.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/cards")
    public ResponseEntity<List<ReviewDeckResponse>> getReviewCards(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) CardType type,
            @RequestParam(required = false) Skill skill) {
        return ResponseEntity.ok(reviewService.getReviewCards(date, type, skill));
    }
} 