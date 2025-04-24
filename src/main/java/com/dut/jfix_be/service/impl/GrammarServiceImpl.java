package com.dut.jfix_be.service.impl;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.response.GrammarListResponse;
import com.dut.jfix_be.dto.response.GrammarResponse;
import com.dut.jfix_be.entity.Grammar;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.repository.GrammarRepository;
import com.dut.jfix_be.service.GrammarService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GrammarServiceImpl implements GrammarService {

    private final GrammarRepository grammarRepository;

    public GrammarServiceImpl(GrammarRepository grammarRepository) {
        this.grammarRepository = grammarRepository;
    }

    @Override
    public List<GrammarResponse> findByLevel(JlptLevel level) {
        List<Grammar> grammars = grammarRepository.findByLevel(level);
        return grammars.stream()
                .map(GrammarResponse::fromGrammar)
                .collect(Collectors.toList());
    }

    @Override
    public DataWithPageResponse<GrammarResponse> getGrammarsByLevelWithLimit(JlptLevel level, int numberOfRecords, int page) {
        if (level == null || numberOfRecords <= 0 || page < 0) {
            return DataWithPageResponse.<GrammarResponse>builder()
                    .data(Collections.emptyList())
                    .totalRecords(0L)
                    .totalPages(0)
                    .nextPage(null)
                    .previousPage(null)
                    .build();
        }

        long totalRecords = grammarRepository.countByLevel(level);
        int totalPages = (int) Math.ceil((double) totalRecords / numberOfRecords);
        Pageable pageable = PageRequest.of(page, numberOfRecords);

        List<GrammarResponse> grammars = grammarRepository.findByLevelWithPageable(level, pageable)
                .getContent()
                .stream()
                .map(GrammarResponse::fromGrammar)
                .collect(Collectors.toList());

        Integer nextPage = (page < totalPages - 1) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;

        return DataWithPageResponse.<GrammarResponse>builder()
                .data(grammars)
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }
}
