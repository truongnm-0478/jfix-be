package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.Deck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer userId;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;

    public static DeckResponse fromDeck(Deck deck) {
        return DeckResponse.builder()
                .id(deck.getId())
                .name(deck.getName())
                .description(deck.getDescription())
                .userId(deck.getUserId())
                .createDate(deck.getCreateDate())
                .createBy(deck.getCreateBy())
                .updateDate(deck.getUpdateDate())
                .updateBy(deck.getUpdateBy())
                .build();
    }
} 