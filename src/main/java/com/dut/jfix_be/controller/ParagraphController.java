package com.dut.jfix_be.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.response.ParagraphResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.ParagraphService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/paragraphs")
@RequiredArgsConstructor
public class ParagraphController {
    private final ParagraphService paragraphService;

    @GetMapping
    public ResponseEntity<ApiResponse<DataWithPageResponse<ParagraphResponse>>> getAllParagraphs(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "level", required = false) JlptLevel level,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<ParagraphResponse> result = paragraphService.getAllParagraphs(keyword, level, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParagraphResponse>> getParagraph(@PathVariable Integer id) {
        ParagraphResponse paragraph = paragraphService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(paragraph));
    }
}