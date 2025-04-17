package com.dut.jfix_be.dto.response;

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
}
