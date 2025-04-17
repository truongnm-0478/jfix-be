package com.dut.jfix_be.controller;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.response.VocabularyResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.VocabularyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<ApiResponse<VocabularyResponse>> createVocabulary(@Valid @RequestBody VocabularyResponse vocabularyResponse) {
        VocabularyResponse createdVocabulary = vocabularyService.createVocabulary(vocabularyResponse);
        return ResponseEntity.ok(ApiResponse.success(createdVocabulary));
    }
}