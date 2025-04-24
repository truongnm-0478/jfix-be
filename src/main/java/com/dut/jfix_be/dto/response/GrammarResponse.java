package com.dut.jfix_be.dto.response;

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
public class GrammarResponse {
    private Integer id;
    private String romaji;
    private String structure;
    private String usage;
    private String meaning;
    private String example;
    private String exampleMeaning;
    private JlptLevel level;

    public static GrammarResponse fromGrammar(Grammar grammar) {
        return GrammarResponse.builder()
                .id(grammar.getId())
                .romaji(grammar.getRomaji())
                .structure(grammar.getStructure())
                .usage(grammar.getUsage())
                .meaning(grammar.getMeaning())
                .example(grammar.getExample())
                .exampleMeaning(grammar.getExampleMeaning())
                .level(grammar.getLevel())
                .build();
    }
}
