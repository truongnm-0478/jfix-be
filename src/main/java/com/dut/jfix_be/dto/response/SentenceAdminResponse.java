package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.Sentence;
import com.dut.jfix_be.enums.JlptLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentenceAdminResponse {
    private Integer id;
    private String japaneseText;
    private String vietnameseText;
    private JlptLevel level;
    private String audioUrl;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private LocalDateTime deleteDate;
    private String deleteBy;

    public static SentenceAdminResponse fromSentence(Sentence s) {
        return SentenceAdminResponse.builder()
                .id(s.getId())
                .japaneseText(s.getJapaneseText())
                .vietnameseText(s.getVietnameseText())
                .level(s.getLevel())
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