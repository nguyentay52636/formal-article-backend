package org.example.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service phụ trách gửi các email hệ thống.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String defaultFrom;

    @Value("${app.url:http://localhost:8080}")
    private String appUrl;

    /**
     * Gửi email xác thực đăng ký tới người dùng mới.
     *
     * @param user thông tin người dùng vừa đăng ký
     * @param verificationToken token xác thực email
     */
    public void sendEmailVerification(User user, String verificationToken) {
        Context context = new Context(new Locale("vi"));
        context.setVariable("fullName", user.getFullName() != null ? user.getFullName() : user.getEmail());
        context.setVariable("email", user.getEmail());
        // Link trỏ đến frontend, frontend sẽ gọi API verify
        context.setVariable("verificationUrl", appUrl + "/xac-thuc-email?token=" + verificationToken);
        context.setVariable("appUrl", appUrl);

        String htmlBody = templateEngine.process("email-verification", context);
        sendHtmlEmail(user.getEmail(), "Xác thực email đăng ký tài khoản", htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            var mimeMessage = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());
            helper.setFrom(defaultFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException ex) {
            log.error("Không thể gửi email tới {}: {}", to, ex.getMessage(), ex);
            throw new IllegalStateException("Không thể gửi email xác nhận đăng ký. Vui lòng thử lại sau.");
        }
    }
}

