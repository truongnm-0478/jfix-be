package com.dut.jfix_be.dto.response;

import java.util.List;

import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.Skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDeckResponse {
    private CardType type;
    private Skill skill;
    private String deckName;
    private List<ReviewCardResponse> cards;
} 