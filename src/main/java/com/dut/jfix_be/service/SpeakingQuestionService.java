package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.SpeakingQuestionRequest;
import com.dut.jfix_be.dto.response.SpeakingQuestionAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;

public interface SpeakingQuestionService {
    DataWithPageResponse<SpeakingQuestionAdminResponse> getAllSpeakingQuestionsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir);
    SpeakingQuestionAdminResponse getSpeakingQuestionDetailForAdmin(Integer id);
    SpeakingQuestionAdminResponse createSpeakingQuestionForAdmin(SpeakingQuestionRequest request);
    SpeakingQuestionAdminResponse updateSpeakingQuestionForAdmin(Integer id, SpeakingQuestionRequest request);
    void deleteSpeakingQuestionForAdmin(Integer id);
}