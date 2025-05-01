package com.dut.jfix_be.dto.request;

import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.Skill;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    @NotNull(message = "error.card.deck.required")
    private Integer deckId;
    
    @NotNull(message = "error.card.type.required")
    private CardType type;
    
    @NotNull(message = "error.card.item.required")
    private Integer itemId;
    
    @NotNull(message = "error.card.skill.required")
    private Skill skill;
} 