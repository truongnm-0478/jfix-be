package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.ParagraphRequest;
import com.dut.jfix_be.dto.response.ParagraphAdminResponse;
import com.dut.jfix_be.dto.response.ParagraphResponse;
import com.dut.jfix_be.enums.JlptLevel;

public interface ParagraphService {
    ParagraphResponse findById(Integer id);
    DataWithPageResponse<ParagraphResponse> getAllParagraphs(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir);
    DataWithPageResponse<ParagraphAdminResponse> getAllParagraphsForAdmin(String keyword, JlptLevel level, int page, int size, String sortBy, String sortDir);
    ParagraphAdminResponse getParagraphDetailForAdmin(Integer id);
    ParagraphAdminResponse createParagraphForAdmin(ParagraphRequest request);
    ParagraphAdminResponse updateParagraphForAdmin(Integer id, ParagraphRequest request);
    void deleteParagraphForAdmin(Integer id);
}