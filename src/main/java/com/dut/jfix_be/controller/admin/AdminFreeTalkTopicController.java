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
import com.dut.jfix_be.dto.request.FreeTalkTopicRequest;
import com.dut.jfix_be.dto.response.FreeTalkTopicAdminResponse;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.service.FreeTalkTopicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/free-talk-topics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFreeTalkTopicController {
    private final FreeTalkTopicService freeTalkTopicService;

    @GetMapping
    public ResponseEntity<ApiResponse<DataWithPageResponse<FreeTalkTopicAdminResponse>>> getAllFreeTalkTopicsForAdmin(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "level", required = false) JlptLevel level,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<FreeTalkTopicAdminResponse> result = freeTalkTopicService.getAllFreeTalkTopicsForAdmin(keyword, level, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FreeTalkTopicAdminResponse>> getFreeTalkTopicDetailForAdmin(@PathVariable Integer id) {
        FreeTalkTopicAdminResponse t = freeTalkTopicService.getFreeTalkTopicDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(t));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FreeTalkTopicAdminResponse>> createFreeTalkTopicForAdmin(@ModelAttribute FreeTalkTopicRequest request) {
        FreeTalkTopicAdminResponse created = freeTalkTopicService.createFreeTalkTopicForAdmin(request);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FreeTalkTopicAdminResponse>> updateFreeTalkTopicForAdmin(@PathVariable Integer id, @ModelAttribute FreeTalkTopicRequest request) {
        FreeTalkTopicAdminResponse updated = freeTalkTopicService.updateFreeTalkTopicForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFreeTalkTopicForAdmin(@PathVariable Integer id) {
        freeTalkTopicService.deleteFreeTalkTopicForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}