# 🔧 Sửa lỗi Database - Thiếu cột email_verification

## ❌ Lỗi hiện tại:
```
Schema-validation: missing column [email_verification_token] in table [user]
```

## ✅ Cách 1: Chạy Migration SQL (Khuyến nghị)

### Bước 1: Mở MySQL Command Line hoặc MySQL Workbench
```bash
mysql -u root -p
```

### Bước 2: Chọn database
```sql
USE hoso;
```

### Bước 3: Chạy SQL migration
```sql
ALTER TABLE `user` 
ADD COLUMN `email_verified` TINYINT(1) NOT NULL DEFAULT 0 AFTER `active`,
ADD COLUMN `email_verification_token` VARCHAR(500) NULL AFTER `email_verified`;

-- Cập nhật các user hiện có (nếu có) - đánh dấu đã verified
UPDATE `user` SET `email_verified` = 1 WHERE `active` = 1;
```

### Bước 4: Kiểm tra
```sql
DESCRIBE user;
```
Bạn sẽ thấy 2 cột mới: `email_verified` và `email_verification_token`

### Bước 5: Chạy lại ứng dụng
```bash
mvn spring-boot:run
```

---

## ✅ Cách 2: Tạm thời để Hibernate tự tạo (Nhanh nhưng không khuyến nghị)

### Chỉnh sửa `application.properties`:
```properties
# Đổi từ validate sang update (tạm thời)
spring.jpa.hibernate.ddl-auto=update
```

**Lưu ý:** 
- Hibernate sẽ tự động tạo cột thiếu
- Sau khi chạy xong, nên đổi lại thành `validate` để an toàn
- Không dùng `update` trong production!

---

## 🎯 Khuyến nghị

**Dùng Cách 1** (chạy SQL migration) vì:
- ✅ An toàn hơn
- ✅ Kiểm soát được schema
- ✅ Phù hợp với production
- ✅ Có thể rollback nếu cần

**Chỉ dùng Cách 2** khi:
- ⚠️ Đang development và muốn nhanh
- ⚠️ Không có quyền truy cập MySQL trực tiếp

