package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    
    @GetMapping("/send-mail")
    public String sendMail() {
        try {
            emailService.sendEmail(
                    "phuongtay52636@gmail.com",         
                    "Thông báo từ hệ thống",    
                    "Người dùng"
            );

            return "✅ Đã gửi email thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Lỗi gửi email: " + e.getMessage();
        }
    }
}
