package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.SentenceRequest;
import com.dut.jfix_be.dto.response.SentenceAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;

public interface SentenceService {
    DataWithPageResponse<SentenceAdminResponse> getAllSentencesForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir);
    SentenceAdminResponse getSentenceDetailForAdmin(Integer id);
    SentenceAdminResponse createSentenceForAdmin(SentenceRequest request);
    SentenceAdminResponse updateSentenceForAdmin(Integer id, SentenceRequest request);
    void deleteSentenceForAdmin(Integer id);
}