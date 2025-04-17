package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.response.VocabularyResponse;
import com.dut.jfix_be.enums.JlptLevel;

import java.util.List;

public interface VocabularyService {
    VocabularyResponse findById(Integer id);
    List<VocabularyResponse> findByLevel(JlptLevel level);
    VocabularyResponse createVocabulary(VocabularyResponse vocabularyResponse);
}