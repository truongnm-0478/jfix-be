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
import com.dut.jfix_be.dto.request.SpeakingQuestionRequest;
import com.dut.jfix_be.dto.response.SpeakingQuestionAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.SpeakingQuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/speaking-questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSpeakingQuestionController {
    private final SpeakingQuestionService speakingQuestionService;

    @GetMapping
    public ResponseEntity<ApiResponse<DataWithPageResponse<SpeakingQuestionAdminResponse>>> getAllSpeakingQuestionsForAdmin(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "level", required = false) JlptLevel level,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<SpeakingQuestionAdminResponse> result = speakingQuestionService.getAllSpeakingQuestionsForAdmin(keyword, level, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpeakingQuestionAdminResponse>> getSpeakingQuestionDetailForAdmin(@PathVariable Integer id) {
        SpeakingQuestionAdminResponse q = speakingQuestionService.getSpeakingQuestionDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(q));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SpeakingQuestionAdminResponse>> createSpeakingQuestionForAdmin(@ModelAttribute SpeakingQuestionRequest request) {
        SpeakingQuestionAdminResponse created = speakingQuestionService.createSpeakingQuestionForAdmin(request);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SpeakingQuestionAdminResponse>> updateSpeakingQuestionForAdmin(@PathVariable Integer id, @ModelAttribute SpeakingQuestionRequest request) {
        SpeakingQuestionAdminResponse updated = speakingQuestionService.updateSpeakingQuestionForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSpeakingQuestionForAdmin(@PathVariable Integer id) {
        speakingQuestionService.deleteSpeakingQuestionForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}