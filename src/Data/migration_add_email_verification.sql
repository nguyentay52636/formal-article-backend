-- Migration: Thêm các trường xác thực email vào bảng user
-- Chạy script này để cập nhật database

ALTER TABLE `user` 
ADD COLUMN `email_verified` TINYINT(1) NOT NULL DEFAULT 0 AFTER `active`,
ADD COLUMN `email_verification_token` VARCHAR(500) NULL AFTER `email_verified`;

-- Cập nhật các user hiện có (nếu có) - đánh dấu đã verified
UPDATE `user` SET `email_verified` = 1 WHERE `active` = 1;

