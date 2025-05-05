package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.Paragraph;
import com.dut.jfix_be.enums.JlptLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphAdminResponse {
    private Integer id;
    private String japaneseText;
    private String vietnameseText;
    private JlptLevel level;
    private String topic;
    private String audioUrl;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private LocalDateTime deleteDate;
    private String deleteBy;

    public static ParagraphAdminResponse fromParagraph(Paragraph paragraph) {
        return ParagraphAdminResponse.builder()
                .id(paragraph.getId())
                .japaneseText(paragraph.getJapaneseText())
                .vietnameseText(paragraph.getVietnameseText())
                .level(paragraph.getLevel())
                .topic(paragraph.getTopic())
                .audioUrl(paragraph.getAudioUrl())
                .createDate(paragraph.getCreateDate())
                .createBy(paragraph.getCreateBy())
                .updateDate(paragraph.getUpdateDate())
                .updateBy(paragraph.getUpdateBy())
                .deleteDate(paragraph.getDeleteDate())
                .deleteBy(paragraph.getDeleteBy())
                .build();
    }
} 