package com.dut.jfix_be.dto.response;

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
public class VocabularyResponse {
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

    public static VocabularyResponse fromVocabulary(Vocabulary vocabulary) {
        return VocabularyResponse.builder()
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
                .build();
    }
}
