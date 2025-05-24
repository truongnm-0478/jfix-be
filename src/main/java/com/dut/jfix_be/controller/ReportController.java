package com.dut.jfix_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.request.ReportRequest;
import com.dut.jfix_be.dto.response.ReportResponse;
import com.dut.jfix_be.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<ReportResponse>> reportError(@Valid @RequestBody ReportRequest request) {
        ReportResponse response = reportService.reportError(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getUnreadReports() {
        List<ReportResponse> response = reportService.getUnreadReports();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getAllReports() {
        List<ReportResponse> response = reportService.getAllReports();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markReportAsRead(@PathVariable Integer id) {
        reportService.markReportAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
} 