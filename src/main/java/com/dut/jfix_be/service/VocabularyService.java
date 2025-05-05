package com.dut.jfix_be.service;

import java.util.List;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.VocabularyAdminRequest;
import com.dut.jfix_be.dto.response.VocabularyAdminResponse;
import com.dut.jfix_be.dto.response.VocabularyResponse;
import com.dut.jfix_be.enums.JlptLevel;

public interface VocabularyService {
    VocabularyResponse findById(Integer id);
    List<VocabularyResponse> findByLevel(JlptLevel level);
    DataWithPageResponse<VocabularyResponse> getAllVocabularies(String keyword, JlptLevel level, String chapter, String section, int page, int size, String sortBy, String sortDir);
    DataWithPageResponse<VocabularyAdminResponse> getAllVocabulariesForAdmin(String keyword, JlptLevel level, String chapter, String section, int page, int size, String sortBy, String sortDir);
    VocabularyAdminResponse getVocabularyDetailForAdmin(Integer id);
    VocabularyAdminResponse createVocabularyForAdmin(VocabularyAdminRequest request);
    VocabularyAdminResponse updateVocabularyForAdmin(Integer id, VocabularyAdminRequest request);
    void deleteVocabularyForAdmin(Integer id);
}