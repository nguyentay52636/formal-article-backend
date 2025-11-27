# 📧 Giới hạn Gmail SMTP và Giải pháp thay thế

## ✅ Cách hoạt động hiện tại

**Có, email sẽ gửi thẳng về Gmail người dùng đăng ký!**

Khi user đăng ký với email `user@gmail.com`:
1. Backend tạo user và token xác thực
2. Gọi `EmailService.sendEmailVerification()`
3. Email được gửi qua Gmail SMTP (`smtp.gmail.com:587`)
4. Email đến hộp thư `user@gmail.com` (có thể vào Spam nếu chưa cấu hình SPF/DKIM)

---

## ⚠️ Giới hạn Gmail SMTP

### Gmail miễn phí (phuongtay52636@gmail.com)
- **500 email/ngày** (tính theo 24h rolling)
- **100 email/giờ** (rate limit)
- Nếu vượt quá → Gmail từ chối, có thể tạm khóa tài khoản

### Google Workspace (Business)
- **2,000 email/ngày**
- **Tốt hơn cho production**

### Lưu ý quan trọng
- Giới hạn tính theo **tài khoản Gmail gửi** (phuongtay52636@gmail.com)
- Không tính theo số người nhận
- Nếu gửi 1 email đến 100 người = 100 email đã dùng

---

## 🛠️ Công nghệ đang dùng

### Stack hiện tại:
```
Spring Boot Mail
    ↓
JavaMailSender (Jakarta Mail API)
    ↓
Gmail SMTP Server (smtp.gmail.com:587)
    ↓
Email đến inbox người dùng
```

**Dependencies:**
- `spring-boot-starter-mail` - Spring Mail integration
- `spring-boot-starter-thymeleaf` - Template engine cho email HTML
- Jakarta Mail API (under the hood)

**Cấu hình:**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=phuongtay52636@gmail.com
spring.mail.password=${MAIL_PASSWORD}  # App Password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 🚀 Giải pháp khi vượt quá giới hạn

### 1. **SendGrid** (Khuyến nghị cho production)
- **100 email/ngày miễn phí** (đủ cho startup)
- **40,000 email/tháng** ở gói trả phí ($15/tháng)
- API dễ dùng, deliverability tốt
- Có dashboard theo dõi

**Cách tích hợp:**
```xml
<!-- Thêm vào pom.xml -->
<dependency>
    <groupId>com.sendgrid</groupId>
    <artifactId>sendgrid-java</artifactId>
    <version>4.10.1</version>
</dependency>
```

```java
// SendGridService.java
@Service
public class SendGridService {
    @Value("${sendgrid.api.key}")
    private String apiKey;
    
    public void sendEmail(String to, String subject, String htmlContent) {
        Email from = new Email("noreply@yourdomain.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);
        
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        
        Response response = sg.api(request);
    }
}
```

### 2. **Amazon SES** (AWS)
- **62,000 email/tháng miễn phí** (nếu chạy trên EC2)
- **$0.10 cho 1,000 email** sau đó
- Rất rẻ, scale tốt
- Cần verify domain

### 3. **Mailgun**
- **5,000 email/tháng miễn phí** (3 tháng đầu)
- **$35/tháng** cho 50,000 email
- API đơn giản, deliverability tốt

### 4. **Resend** (Modern, Developer-friendly)
- **3,000 email/tháng miễn phí**
- **$20/tháng** cho 50,000 email
- API hiện đại, dễ tích hợp
- Tốt cho React/Next.js projects

### 5. **Nâng cấp Google Workspace**
- **$6/user/tháng** → 2,000 email/ngày
- Giữ nguyên code hiện tại
- Chỉ cần đổi email gửi

---

## 📊 So sánh các giải pháp

| Giải pháp | Miễn phí | Trả phí | Dễ tích hợp | Deliverability |
|-----------|----------|---------|------------|----------------|
| **Gmail SMTP** | 500/ngày | - | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| **SendGrid** | 100/ngày | $15/tháng | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Amazon SES** | 62k/tháng* | $0.10/1k | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Mailgun** | 5k/tháng** | $35/tháng | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Resend** | 3k/tháng | $20/tháng | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

*Nếu chạy trên EC2  
**3 tháng đầu

---

## 💡 Khuyến nghị

### Giai đoạn hiện tại (Development/Testing)
✅ **Dùng Gmail SMTP** - Đủ cho testing, miễn phí

### Khi launch (Production)
1. **Nếu < 100 user/ngày**: Tiếp tục dùng Gmail SMTP
2. **Nếu 100-500 user/ngày**: 
   - Nâng cấp Google Workspace ($6/tháng)
   - Hoặc dùng SendGrid free tier
3. **Nếu > 500 user/ngày**: 
   - **SendGrid** hoặc **Resend** (dễ tích hợp nhất)
   - **Amazon SES** (rẻ nhất nếu scale lớn)

---

## 🔧 Cách monitor giới hạn Gmail

### Thêm logging để theo dõi:
```java
@Service
public class EmailService {
    private static final AtomicInteger dailyEmailCount = new AtomicInteger(0);
    private static LocalDate lastResetDate = LocalDate.now();
    
    public void sendEmailVerification(User user, String token) {
        // Reset counter mỗi ngày
        if (!LocalDate.now().equals(lastResetDate)) {
            dailyEmailCount.set(0);
            lastResetDate = LocalDate.now();
        }
        
        int count = dailyEmailCount.incrementAndGet();
        
        if (count > 450) { // Cảnh báo khi gần giới hạn
            log.warn("⚠️ Đã gửi {} email hôm nay. Gần đạt giới hạn 500 email/ngày!", count);
        }
        
        if (count >= 500) {
            throw new IllegalStateException("Đã đạt giới hạn 500 email/ngày của Gmail. Vui lòng thử lại sau.");
        }
        
        // Gửi email...
    }
}
```

---

## 🎯 Kết luận

**Hiện tại:**
- ✅ Email gửi thẳng về Gmail người dùng đăng ký
- ✅ Dùng **Spring Mail + Gmail SMTP**
- ✅ Giới hạn: **500 email/ngày** (Gmail miễn phí)
- ✅ Đủ cho development và testing

**Khi cần scale:**
- Chuyển sang **SendGrid** hoặc **Resend** (dễ tích hợp, free tier tốt)
- Hoặc nâng cấp **Google Workspace** (giữ nguyên code)

**Lưu ý:** Nếu dự án có nhiều user, nên chuyển sớm để tránh bị giới hạn!

