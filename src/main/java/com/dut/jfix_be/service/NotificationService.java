package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.response.ReportDetailResponse;
import com.dut.jfix_be.entity.Report;
import com.dut.jfix_be.entity.User;

public interface NotificationService {
    void sendReportStatusUpdate(String userId, String reportId, String status);
    void sendReportNotification(String userId, String reportId, String message);
    void notifyAdminsOfNewReport(Report report, User reportingUser);
    ReportDetailResponse convertToDetailResponse(Report report, User user);
}