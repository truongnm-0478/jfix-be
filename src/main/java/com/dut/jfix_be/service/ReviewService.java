package com.dut.jfix_be.service;

import java.time.LocalDate;
import java.util.List;

import com.dut.jfix_be.dto.response.ReviewDeckResponse;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.Skill;

public interface ReviewService {
    List<ReviewDeckResponse> getReviewCards(LocalDate date, CardType type, Skill skill);
} 