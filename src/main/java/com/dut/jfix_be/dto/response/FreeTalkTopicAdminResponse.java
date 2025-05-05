package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;

import com.dut.jfix_be.entity.FreeTalkTopic;
import com.dut.jfix_be.enums.JlptLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeTalkTopicAdminResponse {
    private Integer id;
    private String japaneseText;
    private String vietnameseText;
    private JlptLevel level;
    private String conversationPrompt;
    private String sampleAnswerVietnamese;
    private String audioUrl;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private LocalDateTime deleteDate;
    private String deleteBy;

    public static FreeTalkTopicAdminResponse fromFreeTalkTopic(FreeTalkTopic t) {
        return FreeTalkTopicAdminResponse.builder()
                .id(t.getId())
                .japaneseText(t.getJapaneseText())
                .vietnameseText(t.getVietnameseText())
                .level(t.getLevel())
                .conversationPrompt(t.getConversationPrompt())
                .sampleAnswerVietnamese(t.getSampleAnswerVietnamese())
                .audioUrl(t.getAudioUrl())
                .createDate(t.getCreateDate())
                .createBy(t.getCreateBy())
                .updateDate(t.getUpdateDate())
                .updateBy(t.getUpdateBy())
                .deleteDate(t.getDeleteDate())
                .deleteBy(t.getDeleteBy())
                .build();
    }
}