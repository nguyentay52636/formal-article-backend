# 📧 Hướng dẫn cấu hình Gmail SMTP

## ❌ SAI - Không thể đặt tùy ý

```properties
# ❌ SAI - Không hoạt động
spring.mail.username=@gmail.com
spring.mail.username=myapp@gmail.com  # Email không tồn tại
spring.mail.username=anything@gmail.com  # Email giả
```

## ✅ ĐÚNG - Phải dùng email Gmail thật

```properties
# ✅ ĐÚNG - Email Gmail thật của bạn
spring.mail.username=phuongtay52636@gmail.com
spring.mail.username=yourname@gmail.com  # Email Gmail thật của bạn
```

---

## 🔑 Tại sao phải dùng email Gmail thật?

1. **Gmail SMTP yêu cầu xác thực**
   - Phải đăng nhập bằng tài khoản Gmail thật
   - Không thể dùng email giả/tùy ý

2. **App Password**
   - Phải tạo App Password từ tài khoản Gmail thật
   - App Password chỉ hoạt động với email đã tạo nó

3. **Giới hạn Gmail**
   - 500 email/ngày (Gmail miễn phí)
   - Tính theo tài khoản Gmail gửi

---

## 📝 Cách thiết lập

### Bước 1: Có email Gmail thật
- Đăng ký Gmail tại: https://accounts.google.com/signup
- Hoặc dùng email Gmail hiện có của bạn

### Bước 2: Bật 2-Step Verification
1. Vào: https://myaccount.google.com/security
2. Bật **2-Step Verification**
3. Làm theo hướng dẫn (có thể dùng số điện thoại)

### Bước 3: Tạo App Password
1. Vào: https://myaccount.google.com/apppasswords
2. Chọn:
   - **App**: Mail
   - **Device**: Other (Custom name) → Nhập tên: "Formal Article Backend"
3. Click **Generate**
4. Copy mã 16 ký tự (ví dụ: `abcd efgh ijkl mnop`)

### Bước 4: Cấu hình trong code

```properties
# application.properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=yourname@gmail.com  # Email Gmail thật của bạn
spring.mail.password=${MAIL_PASSWORD:}    # App Password (16 ký tự)
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com
```

### Bước 5: Set biến môi trường

**Windows:**
```cmd
setx MAIL_PASSWORD "abcd efgh ijkl mnop"
```

**Linux/Mac:**
```bash
export MAIL_PASSWORD="abcd efgh ijkl mnop"
```

**Hoặc trong IDE:**
- IntelliJ: Run → Edit Configurations → Environment variables
- VS Code: `.env` file (nếu dùng)

---

## 🎯 Ví dụ cụ thể

### Email của bạn: `phuongtay52636@gmail.com`

```properties
# ✅ ĐÚNG
spring.mail.username=phuongtay52636@gmail.com
```

### App Password: `abcd efgh ijkl mnop`

```cmd
# Set biến môi trường (Windows)
setx MAIL_PASSWORD "abcd efgh ijkl mnop"
```

**Lưu ý:** 
- Không có khoảng trắng trong App Password khi set biến môi trường
- Hoặc dùng: `"abcdefghijklmnop"` (bỏ khoảng trắng)

---

## ❓ Câu hỏi thường gặp

### Q: Có thể dùng email khác không phải Gmail?
**A:** Có, nhưng cần cấu hình SMTP khác:
- Outlook: `smtp-mail.outlook.com:587`
- Yahoo: `smtp.mail.yahoo.com:587`
- Custom domain: Cần cấu hình SMTP server riêng

### Q: Có thể dùng email giả/tùy ý không?
**A:** ❌ KHÔNG. Phải dùng email thật đã đăng ký và có App Password.

### Q: Có thể dùng nhiều email Gmail không?
**A:** Có, nhưng mỗi email cần:
- Bật 2-Step Verification
- Tạo App Password riêng
- Cấu hình riêng trong code

### Q: Email gửi đi sẽ hiển thị từ đâu?
**A:** Từ email bạn đặt trong `spring.mail.username`
- Nếu `username=phuongtay52636@gmail.com`
- Email gửi đi sẽ hiển thị: **From: phuongtay52636@gmail.com**

---

## 🔒 Bảo mật

### ✅ Nên làm:
- Dùng biến môi trường cho App Password
- Không commit password vào Git
- Dùng `.gitignore` để bỏ qua `.env` files

### ❌ Không nên:
- Hardcode password trong code
- Commit password lên Git
- Chia sẻ App Password công khai

---

## 📋 Checklist

- [ ] Có email Gmail thật
- [ ] Đã bật 2-Step Verification
- [ ] Đã tạo App Password
- [ ] Đã set biến môi trường `MAIL_PASSWORD`
- [ ] Đã cấu hình `spring.mail.username` = email Gmail thật
- [ ] Test gửi email thành công

---

## 🐛 Xử lý lỗi

### Lỗi: "Authentication failed"
- ❌ App Password sai
- ❌ Email không đúng
- ✅ Kiểm tra lại App Password và email

### Lỗi: "Username and Password not accepted"
- ❌ Chưa bật 2-Step Verification
- ❌ Dùng mật khẩu thường thay vì App Password
- ✅ Bật 2-Step Verification và tạo App Password

### Lỗi: "Could not connect to SMTP host"
- ❌ Firewall chặn port 587
- ❌ Internet không ổn định
- ✅ Kiểm tra kết nối mạng và firewall

---

**Tóm lại: Phải dùng email Gmail thật, không thể đặt tùy ý!** ✅

