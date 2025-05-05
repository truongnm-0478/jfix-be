package com.dut.jfix_be.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.dut.jfix_be.enums.JlptLevel;

import lombok.Data;

@Data
public class VocabularyAdminRequest {
    private String word;
    private String reading;
    private String meaning;
    private String exampleWithReading;
    private String exampleWithoutReading;
    private String exampleMeaning;
    private MultipartFile audio;
    private JlptLevel level;
    private String chapter;
    private String section;
} 