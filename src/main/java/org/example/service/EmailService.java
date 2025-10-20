package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, String name) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("phuongtay52636@gmail.com");

        Context context = new Context();
        context.setVariable("name", name);
        String htmlContent = templateEngine.process("email.html", context);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendForgotPasswordEmail(String to, String name, String resetCode) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("🔐 Yêu cầu đặt lại mật khẩu - Hệ thống quản lý bài viết");
        helper.setFrom("phuongtay52636@gmail.com");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("resetCode", resetCode);
        String htmlContent = templateEngine.process("forgot-password.html", context);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendForgotPasswordEmailTest(String to, String name, String resetCode) {
        System.out.println("=== EMAIL FORGOT PASSWORD TEST ===");
        System.out.println("To: " + to);
        System.out.println("Name: " + name);
        System.out.println("Reset Code: " + resetCode);
        System.out.println("Subject: 🔐 Yêu cầu đặt lại mật khẩu - Hệ thống quản lý bài viết");
        System.out.println("Template: forgot-password.html");
        System.out.println("================================");
    }
}