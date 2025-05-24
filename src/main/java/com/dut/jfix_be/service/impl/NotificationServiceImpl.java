package com.dut.jfix_be.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.constants.WebSocketConstants;
import com.dut.jfix_be.dto.response.ReportDetailResponse;
import com.dut.jfix_be.entity.NotificationMessage;
import com.dut.jfix_be.entity.Report;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.enums.NotificationType;
import com.dut.jfix_be.exception.NotificationException;
import com.dut.jfix_be.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendReportNotification(String userId, String reportId, String message) {
        try {
            NotificationMessage notification = buildNotification(
                NotificationType.NEW_REPORT,
                message,
                userId,
                reportId
            );
            
            messagingTemplate.convertAndSend(WebSocketConstants.ADMIN_NOTIFICATIONS_TOPIC, notification);
        } catch (Exception e) {
            String errorMsg = String.format("error.report.notification.failed", reportId);
            throw new NotificationException(errorMsg, e);
        }
    }

    @Override
    public void sendReportStatusUpdate(String userId, String reportId, String status) {
        try {
            NotificationMessage notification = buildNotification(
                NotificationType.REPORT_STATUS_UPDATE,
                String.format("report.status.update", status),
                userId,
                reportId
            );
            
            String userTopic = String.format(WebSocketConstants.USER_NOTIFICATIONS_TOPIC, userId);
            messagingTemplate.convertAndSend(userTopic, notification);
            
        } catch (Exception e) {
            String errorMsg = String.format("error.report.status.update.failed", reportId);
            throw new NotificationException(errorMsg, e);
        }
    }

    private NotificationMessage buildNotification(NotificationType type, String message, String userId, String reportId) {
        return NotificationMessage.builder()
            .type(type.name())
            .message(message)
            .timestamp(System.currentTimeMillis())
            .userId(userId)
            .reportId(reportId)
            .build();
    }

    @Override
    public void notifyAdminsOfNewReport(Report report, User reportingUser) {
        try {
            validateInputs(report, reportingUser);
            var notificationData = buildNotificationMessage(reportingUser, report);
            
            messagingTemplate.convertAndSend(
                WebSocketConstants.ADMIN_NOTIFICATIONS_TOPIC, 
                NotificationMessage.builder()
                    .type(NotificationType.NEW_REPORT.name())
                    .message(notificationData)
                    .timestamp(System.currentTimeMillis())
                    .userId(reportingUser.getId().toString())
                    .reportId(report.getId().toString())
                    .build()
            );
        } catch (Exception e) {
            String errorMsg = String.format("error.report.notification.failed", report.getId());
            throw new NotificationException(errorMsg, e);
        }
    }

    public ReportDetailResponse convertToDetailResponse(Report report, User user) {
        validateInputs(report, user);
        
        ReportDetailResponse response = new ReportDetailResponse();
        response.setId(report.getId());
        response.setItemId(report.getItemId());
        response.setItemType(report.getItemType());
        response.setMessage(report.getContent());
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setUserEmail(user.getEmail());
        response.setCreateDate(report.getCreateDate().toString());
        response.setIsRead(report.getIsRead());
        return response;
    }

    private void validateInputs(Report report, User user) {
        if (report == null) {
            throw new IllegalArgumentException("error.report.null");
        }
        if (user == null) {
            throw new IllegalArgumentException("error.user.null");
        }
    }

    private Map<String, Object> buildNotificationMessage(User user, Report report) {
        var notificationData = new HashMap<String, Object>();
        notificationData.put("reportId", report.getId());
        notificationData.put("reportContent", report.getContent());
        notificationData.put("itemId", report.getItemId());
        notificationData.put("itemType", report.getItemType());
        notificationData.put("userId", user.getId());
        notificationData.put("userName", user.getName());
        notificationData.put("userEmail", user.getEmail());
        notificationData.put("createDate", report.getCreateDate());
        notificationData.put("isRead", report.getIsRead());
        return notificationData;
    }
}
