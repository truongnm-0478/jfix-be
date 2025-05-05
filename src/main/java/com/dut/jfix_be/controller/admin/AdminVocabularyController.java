package com.dut.jfix_be.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.VocabularyAdminRequest;
import com.dut.jfix_be.dto.response.VocabularyAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.VocabularyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/vocabularies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminVocabularyController {
    private final VocabularyService vocabularyService;

    @GetMapping
    public ResponseEntity<ApiResponse<DataWithPageResponse<VocabularyAdminResponse>>> getAllVocabulariesForAdmin(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "level", required = false) JlptLevel level,
        @RequestParam(value = "chapter", required = false) String chapter,
        @RequestParam(value = "section", required = false) String section,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<VocabularyAdminResponse> result = vocabularyService.getAllVocabulariesForAdmin(keyword, level, chapter, section, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VocabularyAdminResponse>> getVocabularyDetailForAdmin(@PathVariable Integer id) {
        VocabularyAdminResponse vocab = vocabularyService.getVocabularyDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(vocab));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VocabularyAdminResponse>> createVocabularyForAdmin(@ModelAttribute VocabularyAdminRequest request) {
        VocabularyAdminResponse created = vocabularyService.createVocabularyForAdmin(request);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VocabularyAdminResponse>> updateVocabularyForAdmin(@PathVariable Integer id, @ModelAttribute VocabularyAdminRequest request) {
        VocabularyAdminResponse updated = vocabularyService.updateVocabularyForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVocabularyForAdmin(@PathVariable Integer id) {
        vocabularyService.deleteVocabularyForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
} 