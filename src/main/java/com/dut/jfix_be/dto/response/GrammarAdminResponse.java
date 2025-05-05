package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.Grammar;
import com.dut.jfix_be.enums.JlptLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrammarAdminResponse {
    private Integer id;
    private String romaji;
    private String structure;
    private String usage;
    private String meaning;
    private String example;
    private String exampleMeaning;
    private JlptLevel level;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private LocalDateTime deleteDate;
    private String deleteBy;

    public static GrammarAdminResponse fromGrammar(Grammar grammar) {
        return GrammarAdminResponse.builder()
                .id(grammar.getId())
                .romaji(grammar.getRomaji())
                .structure(grammar.getStructure())
                .usage(grammar.getUsage())
                .meaning(grammar.getMeaning())
                .example(grammar.getExample())
                .exampleMeaning(grammar.getExampleMeaning())
                .level(grammar.getLevel())
                .createDate(grammar.getCreateDate())
                .createBy(grammar.getCreateBy())
                .updateDate(grammar.getUpdateDate())
                .updateBy(grammar.getUpdateBy())
                .deleteDate(grammar.getDeleteDate())
                .deleteBy(grammar.getDeleteBy())
                .build();
    }
} 