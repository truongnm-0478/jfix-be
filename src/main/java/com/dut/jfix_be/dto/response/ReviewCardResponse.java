package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.enums.Skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCardResponse {
    // Card and study log info
    private Integer id;
    private Integer cardId;
    private CardType type;
    private Skill skill;
    private LocalDateTime reviewDate;
    private Integer repetition;
    private Float intervals;
    private Float easinessFactor;
    private Integer performance;
    
    // Vocabulary specific fields
    private String word;
    private String meaning;
    private String reading;
    private String example;
    private String exampleMeaning;
    private JlptLevel level;
    
    // Grammar specific fields
    private String structure;
    private String usage;
    private String romaji;
    
    // Paragraph/Sentence specific fields
    private String topic;
    
    // Speaking specific fields
    private String sampleAnswerJapanese;
    private String sampleAnswerVietnamese;
    private String guidelines;
    private String questions;
    
    // Additional metadata
    private String explanation;
    private String notes;

    private String audioUrl;
    private String vietnameseText;
    private String japaneseText;

    private UserMistakeHistoryResponse mistakeHistory;

    // Furigana
    private String japaneseTextFurigana;
    private String sampleAnswerJapaneseFurigana;
} 