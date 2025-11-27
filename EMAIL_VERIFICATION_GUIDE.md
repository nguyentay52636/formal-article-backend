# 📧 Hướng dẫn tích hợp xác thực email đăng ký

## 🔄 Luồng xử lý

### Backend (Đã hoàn thành ✅)

1. **Đăng ký** (`POST /api/auth/register`)
   - User đăng ký với email, password, fullName, phone
   - Backend tạo user với `active=false`, `emailVerified=false`
   - Tạo token xác thực (JWT, hết hạn sau 24h)
   - Gửi email xác thực đến email đăng ký
   - Trả về: `{ "message": "...", "email": "..." }`

2. **Xác thực email** (`GET /api/auth/verify-email?token=xxx`)
   - User click link trong email
   - Backend verify token, kích hoạt tài khoản
   - Trả về: `{ "message": "...", "verified": "true" }`

3. **Đăng nhập** (`POST /api/auth/login`)
   - Kiểm tra `emailVerified=true` mới cho phép đăng nhập
   - Nếu chưa verify, trả về lỗi: "Email chưa được xác thực..."

---

## 🎨 Frontend cần xử lý

### 1. Trang đăng ký (`/dang-ky`)

**Sau khi đăng ký thành công:**
```javascript
// Khi POST /api/auth/register thành công
if (response.status === 200) {
  // Hiển thị thông báo
  showMessage("Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.");
  
  // Có thể redirect đến trang thông báo hoặc trang đăng nhập
  // Không tự động đăng nhập (vì chưa verify email)
}
```

### 2. Trang xác thực email (`/xac-thuc-email`)

**Tạo route mới:**
```javascript
// Route: /xac-thuc-email?token=xxx

// Khi component mount, lấy token từ URL
const token = new URLSearchParams(window.location.search).get('token');

// Gọi API verify
fetch(`http://localhost:8000/api/auth/verify-email?token=${token}`, {
  method: 'GET'
})
.then(response => response.json())
.then(data => {
  if (data.verified === "true") {
    // Xác thực thành công
    showSuccessMessage("Xác thực email thành công! Bạn có thể đăng nhập ngay bây giờ.");
    
    // Redirect đến trang đăng nhập sau 2 giây
    setTimeout(() => {
      window.location.href = '/dang-nhap';
    }, 2000);
  }
})
.catch(error => {
  // Xử lý lỗi (token hết hạn, không hợp lệ, etc.)
  showErrorMessage(error.message || "Không thể xác thực email. Vui lòng thử lại.");
});
```

### 3. Trang đăng nhập (`/dang-nhap`)

**Xử lý lỗi email chưa verify:**
```javascript
// Khi POST /api/auth/login
.catch(error => {
  if (error.message.includes("Email chưa được xác thực")) {
    showWarningMessage(
      "Email chưa được xác thực. " +
      "Vui lòng kiểm tra email và click vào link xác thực để kích hoạt tài khoản."
    );
    // Có thể hiển thị nút "Gửi lại email xác thực" (nếu có chức năng này)
  }
});
```

---

## 📝 Cấu hình

### Backend (`application.properties`)
```properties
# URL frontend - cần cập nhật khi deploy
app.url=http://localhost:3000
```

**Khi deploy production:**
- Đổi `app.url` thành domain thật: `https://yourdomain.com`

---

## 🗄️ Database Migration

**Chạy SQL migration:**
```sql
-- File: src/Data/migration_add_email_verification.sql
ALTER TABLE `user` 
ADD COLUMN `email_verified` TINYINT(1) NOT NULL DEFAULT 0 AFTER `active`,
ADD COLUMN `email_verification_token` VARCHAR(500) NULL AFTER `email_verified`;

UPDATE `user` SET `email_verified` = 1 WHERE `active` = 1;
```

---

## ✅ Checklist triển khai

- [x] Backend: Thêm trường `emailVerified`, `emailVerificationToken` vào User
- [x] Backend: Cập nhật `register()` - user mới chưa active
- [x] Backend: Tạo endpoint `GET /api/auth/verify-email`
- [x] Backend: Gửi email xác thực với link
- [x] Backend: Cập nhật `login()` - kiểm tra email verified
- [ ] **Frontend: Tạo trang `/xac-thuc-email`**
- [ ] **Frontend: Xử lý redirect sau khi verify thành công**
- [ ] **Frontend: Hiển thị thông báo khi đăng ký thành công**
- [ ] **Frontend: Xử lý lỗi "Email chưa được xác thực" khi login**
- [ ] Database: Chạy migration SQL
- [ ] Test: Đăng ký → Kiểm tra email → Click link → Verify → Đăng nhập

---

## 🔗 Link trong email

Link trong email sẽ có dạng:
```
http://localhost:3000/xac-thuc-email?token=eyJhbGciOiJIUzUxMiJ9...
```

Frontend cần:
1. Lấy `token` từ query parameter
2. Gọi API `GET /api/auth/verify-email?token=xxx`
3. Hiển thị thông báo thành công
4. Redirect đến trang đăng nhập

---

## 🐛 Xử lý lỗi

### Token hết hạn
- Backend trả về: "Token xác thực đã hết hạn. Vui lòng đăng ký lại."
- Frontend: Hiển thị thông báo và link "Đăng ký lại"

### Token không hợp lệ
- Backend trả về: "Token xác thực không hợp lệ"
- Frontend: Hiển thị thông báo lỗi

### Email đã được xác thực
- Backend trả về: "Email đã được xác thực trước đó"
- Frontend: Hiển thị thông báo và redirect đến đăng nhập

