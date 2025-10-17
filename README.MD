
📰 Formal Article – Hệ thống quản lý bài viết
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-6DB33F?style=for-the-badge&logo=springboot)]()
[![Google API](https://img.shields.io/badge/Google%20API-Enabled-4285F4?style=for-the-badge&logo=google)]()
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)]()
[![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)]()
Formal Article là hệ thống quản lý bài viết dành cho các tổ chức, tòa soạn hoặc cá nhân muốn quản lý nội dung trực tuyến một cách hiệu quả – bảo mật – dễ mở rộng.

Ứng dụng được xây dựng với kiến trúc RESTful API trên nền Spring Boot, hỗ trợ Swagger UI để tài liệu hoá và thử nghiệm API nhanh chóng.
Ngoài ra, hệ thống tích hợp Google API (Drive, Gmail, OAuth2, hoặc Translate tuỳ cấu hình) để mở rộng khả năng xử lý và đồng bộ dữ liệu trên nền tảng Google.

🛠️ Công nghệ sử dụng
Thành phần	Công nghệ
Ngôn ngữ chính	Java 17
Framework Backend	Spring Boot 3.x
API Documentation	Swagger UI (Springdoc OpenAPI 2.x)
Database	MySQL / PostgreSQL (tuỳ cấu hình)
Security	Spring Security + JWT Authentication
External API	Google API (OAuth2 / Drive / Gmail / Translate / Maps...)
Build Tool	Maven
IDE đề xuất	IntelliJ IDEA / VS Code / Eclipse
✨ Tính năng chính

📰 Quản lý bài viết: thêm, sửa, xoá, xem chi tiết, tìm kiếm theo tiêu đề hoặc danh mục

🧑‍💼 Quản lý người dùng: phân quyền theo vai trò (Admin, Editor, Viewer)

🔒 Đăng nhập & xác thực: sử dụng JWT Token, có thể đăng nhập qua Google OAuth2

📁 Tích hợp Google Drive: tự động lưu trữ hình ảnh, file đính kèm của bài viết

📊 Thống kê: thống kê số lượng bài viết, lượt xem, hoạt động người dùng

🌐 API công khai: hỗ trợ truy xuất dữ liệu bài viết qua REST API

🧩 Swagger UI: thử nghiệm trực tiếp các endpoint trong trình duyệt

⚡ Cấu trúc dự án
📦 formal-article
 ┣ 📂 src
 ┃ ┣ 📂 main
 ┃ ┃ ┣ 📂 java/com/example/formalarticle
 ┃ ┃ ┃ ┣ 📂 controller     # Chứa các REST API controller
 ┃ ┃ ┃ ┣ 📂 model          # Entity & DTO
 ┃ ┃ ┃ ┣ 📂 repository     # JPA Repository interface
 ┃ ┃ ┃ ┣ 📂 service        # Business logic
 ┃ ┃ ┃ ┗ 📂 config         # Cấu hình bảo mật, Swagger, Google API, v.v.
 ┃ ┃ ┗ 📂 resources
 ┃ ┃   ┣ 📜 application.yml  # Cấu hình database, API keys, port,...
 ┃ ┃   ┗ 📜 static / templates
 ┣ 📜 pom.xml               # Maven dependencies
 ┣ 📜 README.md             # Tài liệu dự án
 ┗ 📜 .gitignore

🧠 Kiến trúc tổng quan
Frontend (React Next js )
          ⬇️
      RESTful API (Spring Boot)
          ⬇️
Database (MySQL / PostgreSQL)
          ⬇️
 Google API Integration (Drive, Gmail, OAuth2)

🔧 Cài đặt & Chạy dự án
1️⃣ Yêu cầu môi trường

Java 17 trở lên

Maven 3.9+

MySQL hoặc PostgreSQL

Google API Credentials (nếu dùng OAuth2 / Drive / Gmail)

2️⃣ Clone dự án
git clone https://github.com/<your-username>/formal-article.git
cd formal-article
mvn clean install 
mvn spring-boot:run
