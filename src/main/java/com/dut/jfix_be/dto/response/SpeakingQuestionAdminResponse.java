package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.SpeakingQuestion;
import com.dut.jfix_be.enums.JlptLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeakingQuestionAdminResponse {
    private Integer id;
    private String japaneseText;
    private String vietnameseText;
    private JlptLevel level;
    private String sampleAnswerJapanese;
    private String sampleAnswerVietnamese;
    private String audioUrl;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private LocalDateTime deleteDate;
    private String deleteBy;

    public static SpeakingQuestionAdminResponse fromSpeakingQuestion(SpeakingQuestion s) {
        return SpeakingQuestionAdminResponse.builder()
                .id(s.getId())
                .japaneseText(s.getJapaneseText())
                .vietnameseText(s.getVietnameseText())
                .level(s.getLevel())
                .sampleAnswerJapanese(s.getSampleAnswerJapanese())
                .sampleAnswerVietnamese(s.getSampleAnswerVietnamese())
                .audioUrl(s.getAudioUrl())
                .createDate(s.getCreateDate())
                .createBy(s.getCreateBy())
                .updateDate(s.getUpdateDate())
                .updateBy(s.getUpdateBy())
                .deleteDate(s.getDeleteDate())
                .deleteBy(s.getDeleteBy())
                .build();
    }
}