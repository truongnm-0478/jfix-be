package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.Vocabulary;
import com.dut.jfix_be.enums.JlptLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyAdminResponse {
    private Integer id;
    private String word;
    private String reading;
    private String meaning;
    private String exampleWithReading;
    private String exampleWithoutReading;
    private String exampleMeaning;
    private String audio;
    private JlptLevel level;
    private String chapter;
    private String section;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private LocalDateTime deleteDate;
    private String deleteBy;

    public static VocabularyAdminResponse fromVocabulary(Vocabulary vocabulary) {
        return VocabularyAdminResponse.builder()
                .id(vocabulary.getId())
                .word(vocabulary.getWord())
                .reading(vocabulary.getReading())
                .meaning(vocabulary.getMeaning())
                .exampleWithReading(vocabulary.getExampleWithReading())
                .exampleWithoutReading(vocabulary.getExampleWithoutReading())
                .exampleMeaning(vocabulary.getExampleMeaning())
                .audio(vocabulary.getAudio())
                .level(vocabulary.getLevel())
                .chapter(vocabulary.getChapter())
                .section(vocabulary.getSection())
                .createDate(vocabulary.getCreateDate())
                .createBy(vocabulary.getCreateBy())
                .updateDate(vocabulary.getUpdateDate())
                .updateBy(vocabulary.getUpdateBy())
                .deleteDate(vocabulary.getDeleteDate())
                .deleteBy(vocabulary.getDeleteBy())
                .build();
    }
} 