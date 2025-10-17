# 📰 Formal Article – Hệ thống quản lý bài viết

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-6DB33F?style=for-the-badge&logo=springboot)]()
[![Google API](https://img.shields.io/badge/Google%20API-Enabled-4285F4?style=for-the-badge&logo=google)]()
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)]()
[![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)]()

---

## 🧾 Giới thiệu

**Formal Article** là hệ thống quản lý bài viết dành cho **doanh nghiệp, tổ chức báo chí hoặc cá nhân** muốn quản lý nội dung trực tuyến **hiệu quả – bảo mật – dễ mở rộng**.  
Ứng dụng được phát triển với **Spring Boot** và kiến trúc **RESTful API**, hỗ trợ **Swagger UI** giúp tài liệu hóa và thử nghiệm API nhanh chóng.  
Ngoài ra, hệ thống tích hợp **Google API** (Drive, Gmail, OAuth2, Translate, v.v.) để đồng bộ dữ liệu và mở rộng khả năng xử lý trên nền tảng Google.

---

## 🛠️ Công nghệ sử dụng

| Thành phần | Công nghệ |
|-------------|------------|
| **Ngôn ngữ chính** | Java 17 |
| **Framework Backend** | Spring Boot 3.x |
| **API Documentation** | Swagger UI (Springdoc OpenAPI 2.x) |
| **Database** | MySQL / PostgreSQL (tuỳ cấu hình) |
| **Security** | Spring Security + JWT Authentication |
| **External API** | Google API (OAuth2 / Drive / Gmail / Translate / Maps...) |
| **Build Tool** | Maven |
| **IDE đề xuất** | IntelliJ IDEA / VS Code / Eclipse |

---

## ✨ Tính năng chính

- 📰 **Quản lý bài viết**: thêm, sửa, xoá, xem chi tiết, tìm kiếm theo tiêu đề hoặc danh mục.  
- 🧑‍💼 **Quản lý người dùng**: phân quyền theo vai trò *(Admin, Editor, Viewer)*.  
- 🔒 **Đăng nhập & xác thực**: hỗ trợ JWT Token và đăng nhập bằng Google OAuth2.  
- 📁 **Tích hợp Google Drive**: tự động lưu trữ hình ảnh, tệp đính kèm của bài viết.  
- 📊 **Thống kê tổng quan**: thống kê số lượng bài viết, lượt xem, hoạt động người dùng.  
- 🌐 **API công khai**: truy xuất dữ liệu bài viết qua REST API.  
- 🧩 **Swagger UI**: thử nghiệm trực tiếp các endpoint trong trình duyệt.  

---

## ⚡ Cấu trúc dự án

