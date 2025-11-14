SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
SET NAMES utf8mb4;

-- =====================================================
-- 1. file_upload phải đặt trước vì nhiều bảng tham chiếu
-- =====================================================

CREATE TABLE file_upload (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    type ENUM('image','document','other') NOT NULL,
    mime_type VARCHAR(100),
    file_name VARCHAR(255),
    path VARCHAR(500),
    size INT,
    width INT,
    height INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO file_upload (id, user_id, type, mime_type, file_name, path, size, width, height, created_at)
VALUES
(1, NULL, 'image', 'image/png', 'avatar1.png', '/uploads/avatars/avatar1.png', 204800, 400, 400, NOW()),
(2, NULL, 'image', 'image/png', 'template-blue.png', '/uploads/templates/template-blue.png', 350000, 800, 1200, NOW()),
(3, NULL, 'document', 'application/pdf', 'cv_1.pdf', '/uploads/pdf/cv_1.pdf', 550000, NULL, NULL, NOW());

-- =====================================================
-- 2. user
-- =====================================================

CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    avatar_id BIGINT,
    active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (avatar_id) REFERENCES file_upload(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO user (id, email, password, full_name, avatar_id, active, created_at)
VALUES
(1, 'thanhnguyen@gmail.com', '$2a$10$testpasswordhash', 'Nguyễn Thanh', 1, 1, NOW());

-- Cập nhật user_id cho file_upload sau khi có user
UPDATE file_upload SET user_id = 1 WHERE id IN (1,2,3);

-- =====================================================
-- 3. tag (nhóm CV)
-- =====================================================

CREATE TABLE tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    slug VARCHAR(150) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO tag (id, slug, name)
VALUES
(1, 'sinh-vien', 'Sinh viên'),
(2, 'it', 'Công nghệ thông tin'),
(3, 'kinh-doanh', 'Kinh doanh');

-- =====================================================
-- 4. template
-- =====================================================

CREATE TABLE template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(200) UNIQUE NOT NULL,
    summary VARCHAR(300),

    html LONGTEXT,
    css LONGTEXT,

    preview_image_id BIGINT,
    tag_id BIGINT,

    views BIGINT NOT NULL DEFAULT 0,
    downloads BIGINT NOT NULL DEFAULT 0,

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (preview_image_id) REFERENCES file_upload(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO template (id, name, slug, summary, html, css, preview_image_id, tag_id, views, downloads, created_at)
VALUES
(1, 'Mẫu CV Xanh Chuyên Nghiệp', 'cv-xanh-chuyen-nghiep',
 'CV phù hợp cho các vị trí văn phòng và Fresher',
 '<div>{{content}}</div>',
 'body { font-family: Roboto; }',
 2, 2, 1234, 567, NOW());

-- =====================================================
-- 5. generated_cv
-- =====================================================

CREATE TABLE generated_cv (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,

    data_json LONGTEXT NOT NULL CHECK (JSON_VALID(data_json)),
    style_json LONGTEXT NOT NULL CHECK (JSON_VALID(style_json)),

    html_output LONGTEXT,
    pdf_file_id BIGINT,

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES template(id),
    FOREIGN KEY (pdf_file_id) REFERENCES file_upload(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO generated_cv (
    id, user_id, template_id,
    data_json, style_json,
    html_output, pdf_file_id,
    created_at, updated_at
)
VALUES
(
1, 1, 1,
'{
  "profile": {
    "full_name": "Nguyễn Thanh",
    "job_title": "Developer",
    "gender": "Male",
    "birthday": "2000-05-12",
    "phone": "0799356819",
    "email": "phamthanhdenho@gmail.com",
    "address": "TP.HCM",
    "avatar_id": 1
  },
  "objective": "Mong muốn làm việc trong môi trường chuyên nghiệp để phát triển bản thân.",

  "education": [
    {
      "school": "ĐH Công Nghệ",
      "faculty": "Công nghệ thông tin",
      "certificate": "Cử nhân CNTT",
      "training": "Kỹ thuật phần mềm",
      "classification": "Giỏi",
      "start_month": "08",
      "start_year": "2018",
      "end_month": "06",
      "end_year": "2022",
      "description": "Hoàn thành đồ án tốt nghiệp xuất sắc."
    }
  ],

  "experience": [
    {
      "company": "Công ty ABC",
      "position": "Assistant Data Analyst",
      "start_month": "03",
      "start_year": "2022",
      "end_month": "05",
      "end_year": "2023",
      "description": [
        "Phân tích dữ liệu hỗ trợ quyết định kinh doanh.",
        "Sử dụng SQL, Python (Pandas, NumPy) và Excel.",
        "Chuẩn hóa dữ liệu và xây dựng báo cáo trực quan."
      ]
    }
  ],

  "skills": [
    { "name": "Management skill", "level": 4 },
    { "name": "Communication skill", "level": 5 },
    { "name": "Problem solving", "level": 4 }
  ],

  "computer_skills": [
    { "name": "MS Word", "level": 5 }
  ],

  "languages": [
    { "name": "English", "level": 4 }
  ],

  "hobbies": "Đọc sách, nghe nhạc",

  "references": [
    {
      "name": "Nguyễn Văn A",
      "company": "Công ty B",
      "phone": "0900111222"
    }
  ]
}',
'{
  "color": "#205dac",
  "font": "Roboto",
  "layout": "layout-1"
}',
NULL, 3, NOW(), NOW()
);

-- =====================================================
-- 6. favourite_cv
-- =====================================================

CREATE TABLE favourite_cv (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, template_id),

    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES template(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO favourite_cv (id, user_id, template_id, created_at)
VALUES
(1, 1, 1, NOW());

-- =====================================================
-- 7. comment
-- =====================================================

CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    user_id BIGINT,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (template_id) REFERENCES template(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO comment (id, template_id, user_id, content, created_at)
VALUES
(1, 1, 1, 'Mẫu CV rất đẹp và chuyên nghiệp!', NOW());

-- =====================================================
-- 8. rating
-- =====================================================

CREATE TABLE rating (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    user_id BIGINT,
    score TINYINT NOT NULL CHECK (score BETWEEN 1 AND 5),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (template_id, user_id),

    FOREIGN KEY (template_id) REFERENCES template(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO rating (id, template_id, user_id, score, created_at)
VALUES
(1, 1, 1, 5, NOW());
CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO role (id, name, description, created_at, updated_at)
VALUES
(1, 'ADMIN', 'Quản trị hệ thống', NOW(), NOW()),
(2, 'USER', 'Người dùng', NOW(), NOW());



COMMIT;

