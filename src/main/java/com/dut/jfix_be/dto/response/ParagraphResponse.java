package com.dut.jfix_be.dto.response;

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
public class ParagraphResponse {
    private Integer id;
    private String japaneseText;
    private String vietnameseText;
    private JlptLevel level;
    private String topic;
    private String audioUrl;

    public static ParagraphResponse fromParagraph(Paragraph paragraph) {
        return ParagraphResponse.builder()
                .id(paragraph.getId())
                .japaneseText(paragraph.getJapaneseText())
                .vietnameseText(paragraph.getVietnameseText())
                .level(paragraph.getLevel())
                .topic(paragraph.getTopic())
                .audioUrl(paragraph.getAudioUrl())
                .build();
    }
}