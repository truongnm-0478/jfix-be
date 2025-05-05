package com.dut.jfix_be.dto.request;

import com.dut.jfix_be.enums.JlptLevel;

import lombok.Data;

@Data
public class GrammarRequest {
    private String romaji;
    private String structure;
    private String usage;
    private String meaning;
    private String example;
    private String exampleMeaning;
    private JlptLevel level;
} 