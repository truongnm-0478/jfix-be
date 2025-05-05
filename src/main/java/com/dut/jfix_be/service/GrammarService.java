package com.dut.jfix_be.service;

import java.util.List;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.GrammarRequest;
import com.dut.jfix_be.dto.response.GrammarAdminResponse;
import com.dut.jfix_be.dto.response.GrammarResponse;
import com.dut.jfix_be.enums.JlptLevel;

public interface GrammarService {
    List<GrammarResponse> findByLevel(JlptLevel level);
    DataWithPageResponse<GrammarResponse> getGrammarsByLevelWithLimit(JlptLevel level, int numberOfRecords, int page);
    GrammarResponse findById(Integer id);
    DataWithPageResponse<GrammarResponse> getAllGrammars(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir);
    void deleteGrammarForAdmin(Integer id);
    DataWithPageResponse<GrammarAdminResponse> getAllGrammarsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir);
    GrammarAdminResponse getGrammarDetailForAdmin(Integer id);
    GrammarAdminResponse createGrammarForAdmin(GrammarRequest request);
    GrammarAdminResponse updateGrammarForAdmin(Integer id, GrammarRequest request);
}
