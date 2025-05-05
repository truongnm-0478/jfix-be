package com.dut.jfix_be.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.GrammarRequest;
import com.dut.jfix_be.dto.response.GrammarAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.GrammarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/grammars")
@RequiredArgsConstructor
public class AdminGrammarController {
    private final GrammarService grammarService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DataWithPageResponse<GrammarAdminResponse>>> getAllGrammarsForAdmin(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "level", required = false) JlptLevel level,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<GrammarAdminResponse> result = grammarService.getAllGrammarsForAdmin(keyword, level, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GrammarAdminResponse>> getGrammarDetailForAdmin(@PathVariable Integer id) {
        GrammarAdminResponse grammar = grammarService.getGrammarDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(grammar));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GrammarAdminResponse>> createGrammarForAdmin(@RequestBody GrammarRequest request) {
        GrammarAdminResponse created = grammarService.createGrammarForAdmin(request);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GrammarAdminResponse>> updateGrammarForAdmin(@PathVariable Integer id, @RequestBody GrammarRequest request) {
        GrammarAdminResponse updated = grammarService.updateGrammarForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGrammarForAdmin(@PathVariable Integer id) {
        grammarService.deleteGrammarForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
} 