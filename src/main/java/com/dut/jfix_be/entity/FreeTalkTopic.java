package com.dut.jfix_be.entity;

import java.time.LocalDateTime;

import com.dut.jfix_be.enums.JlptLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "free_talk_topics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeTalkTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "japanese_text", nullable = false)
    private String japaneseText;

    @Column(name = "vietnamese_text", nullable = false)
    private String vietnameseText;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    @Builder.Default
    private JlptLevel level = JlptLevel.FREE;

    @Column(name = "conversation_prompt")
    private String conversationPrompt;

    @Column(name = "sample_answer_vietnamese")
    private String sampleAnswerVietnamese;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "create_date", nullable = false)
    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "create_by", nullable = false)
    private String createBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_by")
    private String deleteBy;
}