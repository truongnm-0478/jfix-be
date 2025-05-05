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
import com.dut.jfix_be.dto.request.SentenceRequest;
import com.dut.jfix_be.dto.response.SentenceAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.SentenceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/sentences")
@RequiredArgsConstructor
public class AdminSentenceController {
    private final SentenceService sentenceService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DataWithPageResponse<SentenceAdminResponse>>> getAllSentencesForAdmin(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "level", required = false) JlptLevel level,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<SentenceAdminResponse> result = sentenceService.getAllSentencesForAdmin(keyword, level, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SentenceAdminResponse>> getSentenceDetailForAdmin(@PathVariable Integer id) {
        SentenceAdminResponse sentence = sentenceService.getSentenceDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(sentence));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SentenceAdminResponse>> createSentenceForAdmin(@ModelAttribute SentenceRequest request) {
        SentenceAdminResponse created = sentenceService.createSentenceForAdmin(request);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SentenceAdminResponse>> updateSentenceForAdmin(@PathVariable Integer id, @ModelAttribute SentenceRequest request) {
        SentenceAdminResponse updated = sentenceService.updateSentenceForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSentenceForAdmin(@PathVariable Integer id) {
        sentenceService.deleteSentenceForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}