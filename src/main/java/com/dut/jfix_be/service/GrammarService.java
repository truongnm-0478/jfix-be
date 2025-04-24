package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.response.GrammarListResponse;
import com.dut.jfix_be.dto.response.GrammarResponse;
import com.dut.jfix_be.enums.JlptLevel;

import java.util.List;
import java.util.Map;

public interface GrammarService {
    List<GrammarResponse> findByLevel(JlptLevel level);

    DataWithPageResponse<GrammarResponse> getGrammarsByLevelWithLimit(JlptLevel level, int numberOfRecords, int page);
}
