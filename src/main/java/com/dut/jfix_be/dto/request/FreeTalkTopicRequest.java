package com.dut.jfix_be.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.dut.jfix_be.enums.JlptLevel;

import lombok.Data;

@Data
public class FreeTalkTopicRequest {
    private String japaneseText;
    private String vietnameseText;
    private JlptLevel level;
    private String conversationPrompt;
    private String sampleAnswerVietnamese;
    private MultipartFile audio;
}