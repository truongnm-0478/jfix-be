package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.Card;
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
public class CardResponse {
    private Integer id;
    private Integer deckId;
    private CardType type;
    private Integer itemId;
    private Skill skill;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;

    public static CardResponse fromCard(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .deckId(card.getDeckId())
                .type(card.getType())
                .itemId(card.getItemId())
                .skill(card.getSkill())
                .createDate(card.getCreateDate())
                .createBy(card.getCreateBy())
                .updateDate(card.getUpdateDate())
                .updateBy(card.getUpdateBy())
                .build();
    }
} 