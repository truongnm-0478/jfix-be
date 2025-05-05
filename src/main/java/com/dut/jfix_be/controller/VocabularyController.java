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
import com.dut.jfix_be.dto.response.VocabularyResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.VocabularyService;

@RestController
@RequestMapping("/api/vocabularies")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VocabularyResponse>> getVocabulary(@PathVariable Integer id) {
        VocabularyResponse vocabulary = vocabularyService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(vocabulary));
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<ApiResponse<List<VocabularyResponse>>> getVocabulariesByLevel(@PathVariable JlptLevel level) {
        List<VocabularyResponse> vocabularies = vocabularyService.findByLevel(level);
        return ResponseEntity.ok(ApiResponse.success(vocabularies));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DataWithPageResponse<VocabularyResponse>>> getAllVocabularies(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "level", required = false) JlptLevel level,
        @RequestParam(value = "chapter", required = false) String chapter,
        @RequestParam(value = "section", required = false) String section,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<VocabularyResponse> result = vocabularyService.getAllVocabularies(keyword, level, chapter, section, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}