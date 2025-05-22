package com.dut.jfix_be.service.impl;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String senderEmail;
    
    @Value("${app.name:JFix}")
    private String appName;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, 
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
                    StandardCharsets.UTF_8.name());
            
            Context context = new Context();
            context.setVariable("name", user.getName());
            context.setVariable("resetCode", resetToken);
            context.setVariable("frontendUrl", frontendUrl);
            context.setVariable("appName", appName);
            
            String emailContent = templateEngine.process("password-reset-email", context);
            
            helper.setTo(user.getEmail());
            helper.setSubject(appName + " - Mã xác thực đặt lại mật khẩu");
            helper.setFrom(senderEmail);
            helper.setText(emailContent, true);
            
            mailSender.send(message);
            log.info("Password reset email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}", user.getEmail(), e);
            throw new RuntimeException("Could not send password reset email", e);
        }
    }
} 