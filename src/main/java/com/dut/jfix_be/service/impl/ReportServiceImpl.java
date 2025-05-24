package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dut.jfix_be.dto.request.ReportRequest;
import com.dut.jfix_be.dto.response.ReportResponse;
import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.entity.Report;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.ReportRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.NotificationService;
import com.dut.jfix_be.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final SimpMessagingTemplate messagingTemplate;
    private final ReportRepository reportRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ReportResponse reportError(ReportRequest request) {
        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.card.not.found", new Object[]{request.getCardId()}, LocaleContextHolder.getLocale())
                ));
        String itemType = card.getType().name();
        Integer itemId = card.getItemId();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
                )).getId();

        Report report = Report.builder()
                .userId(userId)
                .cardId(card.getId())
                .itemId(itemId)
                .itemType(itemType)
                .content(request.getContent())
                .createDate(LocalDateTime.now())
                .isRead(false)
                .build();
        reportRepository.save(report);
        
        var notifyObj = new java.util.HashMap<String, Object>();
        notifyObj.put("username", username);
        notifyObj.put("itemType", itemType);
        notifyObj.put("itemId", itemId);
        notifyObj.put("content", request.getContent());
        messagingTemplate.convertAndSend("/topic/admin-report", notifyObj);
        
        notificationService.notifyAdminsOfNewReport(report, userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("error.user.not.found")));
        
        String responseMessage = messageSource.getMessage(
            "report.success",
            null,
            "success.report.sent",
            LocaleContextHolder.getLocale()
        );
        return new ReportResponse(report.getId(), itemId, itemType, request.getContent(), report.getCreateDate().toString(), report.getIsRead());
    }

    @Override
    public List<ReportResponse> getUnreadReports() {
        return reportRepository.findAllUnreadReports().stream()
            .map(report -> {
                User user = userRepository.findById(report.getUserId())
                    .orElseThrow(() -> new RuntimeException("error.user.not.found"));
                return notificationService.convertToDetailResponse(report, user);
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAllReportsOrderByCreateDateDesc().stream()
            .map(report -> {
                User user = userRepository.findById(report.getUserId())
                    .orElseThrow(() -> new RuntimeException("error.report.not.found"));
                return notificationService.convertToDetailResponse(report, user);
            })
            .collect(Collectors.toList());
        
    }

    @Override
    @Transactional
    public void markReportAsRead(Integer id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("error.report.not.found"));
        report.setIsRead(true);
        reportRepository.save(report);
    }
} 