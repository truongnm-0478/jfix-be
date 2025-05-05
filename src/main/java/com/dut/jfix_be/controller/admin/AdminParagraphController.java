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
import com.dut.jfix_be.dto.request.ParagraphRequest;
import com.dut.jfix_be.dto.response.ParagraphAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.ParagraphService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/paragraphs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminParagraphController {
    private final ParagraphService paragraphService;

    @GetMapping
    public ResponseEntity<ApiResponse<DataWithPageResponse<ParagraphAdminResponse>>> getAllParagraphsForAdmin(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "level", required = false) JlptLevel level,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<ParagraphAdminResponse> result = paragraphService.getAllParagraphsForAdmin(keyword, level, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParagraphAdminResponse>> getParagraphDetailForAdmin(@PathVariable Integer id) {
        ParagraphAdminResponse paragraph = paragraphService.getParagraphDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(paragraph));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParagraphAdminResponse>> createParagraphForAdmin(@ModelAttribute ParagraphRequest request) {
        ParagraphAdminResponse created = paragraphService.createParagraphForAdmin(request);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParagraphAdminResponse>> updateParagraphForAdmin(@PathVariable Integer id, @ModelAttribute ParagraphRequest request) {
        ParagraphAdminResponse updated = paragraphService.updateParagraphForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteParagraphForAdmin(@PathVariable Integer id) {
        paragraphService.deleteParagraphForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}