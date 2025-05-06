package com.dut.jfix_be.service;

import java.util.List;

import com.dut.jfix_be.dto.request.ReportRequest;
import com.dut.jfix_be.dto.response.ReportResponse;

public interface ReportService {
    ReportResponse reportError(ReportRequest request);
    List<ReportResponse> getUnreadReports();
    List<ReportResponse> getAllReports();
    void markReportAsRead(Integer id);
} 