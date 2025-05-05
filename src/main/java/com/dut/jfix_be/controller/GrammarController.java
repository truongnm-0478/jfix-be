package com.dut.jfix_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.response.GrammarResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.GrammarService;

@RestController
@RequestMapping("/api/grammars")
public class GrammarController {

    private final GrammarService grammarService;

    public GrammarController(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<ApiResponse<List<GrammarResponse>>> getGrammarsByLevel(@PathVariable JlptLevel level) {
        List<GrammarResponse> grammars = grammarService.findByLevel(level);
        return ResponseEntity.ok(ApiResponse.success(grammars));
    }

    @GetMapping("/level/{level}/limit")
    public ResponseEntity<ApiResponse<DataWithPageResponse<GrammarResponse>>> getGrammarsByLevelWithLimit(
            @PathVariable JlptLevel level,
            @RequestParam("limit") int numberOfRecords,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        DataWithPageResponse<GrammarResponse> result = grammarService.getGrammarsByLevelWithLimit(level, numberOfRecords, page);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<DataWithPageResponse<GrammarResponse>>> getAllGrammars(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "level", required = false) JlptLevel level,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<GrammarResponse> result = grammarService.getAllGrammars(keyword, level, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GrammarResponse>> getGrammar(@PathVariable Integer id) {
        GrammarResponse grammar = grammarService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(grammar));
    }
}
