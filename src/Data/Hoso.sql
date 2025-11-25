SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
SET NAMES utf8mb4;

-- ======================================
-- 1. ROLE
-- ======================================
CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO role (name, description, created_at, updated_at)
VALUES
('ADMIN', 'Quản trị hệ thống', NOW(), NOW()),
('USER', 'Người dùng bình thường', NOW(), NOW()),
('EDITOR', 'Quản lý nội dung', NOW(), NOW()),
('CONSULTANT', 'Tư vấn viên', NOW(), NOW());




-- ======================================
-- 2. USER
-- ======================================
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    phone VARCHAR(10) UNIQUE NOT NULL,
    avatar VARCHAR(500),
    active TINYINT(1) DEFAULT 1,
    role_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO user (email, password, full_name, phone, avatar, active, role_id, created_at)
VALUES
('admin@gmail.com', '$2a$10$pass111', 'Nguyễn Admin', '0900000001', '/uploads/avatars/admin.png', 1, 1, NOW()),
('user1@gmail.com', '$2a$10$pass222', 'Trần User',   '0900000002', '/uploads/avatars/user1.png', 1, 2, NOW()),
('editor@gmail.com', '$2a$10$pass333', 'Lê Editor',  '0900000003', '/uploads/avatars/editor.png', 1, 3, NOW()),
('consultant@gmail.com', '$2a$10$pass444', 'Lê Consultant',  '0900000004', '/uploads/avatars/consultant.png', 1, 4, NOW());


-- ======================================
-- 3. TAG
-- ======================================
CREATE TABLE tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    slug VARCHAR(150) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL
    type VARCHAR(50) NOT NULL,
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO tag (slug, name)
VALUES
('sinh-vien', 'Sinh viên'),
('it', 'Công nghệ thông tin'),
('marketing', 'Marketing');


-- ======================================
-- 4. TEMPLATE
-- ======================================
CREATE TABLE template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(200) UNIQUE NOT NULL,
    summary VARCHAR(300),
    html LONGTEXT,
    css LONGTEXT,
    preview_url VARCHAR(500),
    tag_id BIGINT,
    views BIGINT NOT NULL DEFAULT 0,
    color VARCHAR(500),
    description TEXT,
    language VARCHAR(100),
    usage VARCHAR(200),
    design VARCHAR(150),
    downloads BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (tag_id) REFERENCES tag(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE template_features (
    template_id BIGINT NOT NULL,
    feature VARCHAR(255) DEFAULT NULL,
    FOREIGN KEY (template_id) REFERENCES template(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO template (name, slug, summary, html, css, preview_url, tag_id, views, downloads, created_at)
VALUES
('CV Xanh Chuyên Nghiệp', 'cv-xanh', 'CV phù hợp sinh viên IT',
 '<div>CV</div>', 'body{}', '/uploads/templates/cv1.png', 2, 100, 20, NOW()),

('CV Đơn Giản Trắng', 'cv-trang', 'CV minimal sạch đẹp',
 '<div>CV2</div>', 'body{}', '/uploads/templates/cv2.png', 1, 350, 78, NOW()),

('CV Marketing Sáng Tạo', 'cv-marketing', 'Phong cách sáng tạo cho Marketing',
 '<div>CV3</div>', 'body{}', '/uploads/templates/cv3.png', 3, 500, 120, NOW());


-- ======================================
-- 5. GENERATED_CV
-- ======================================
CREATE TABLE generated_cv (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    data_json LONGTEXT NOT NULL CHECK (JSON_VALID(data_json)),
    style_json LONGTEXT NOT NULL CHECK (JSON_VALID(style_json)),
    html_output LONGTEXT,
    title VARCHAR(200),
    pdf_url VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES template(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO generated_cv (user_id, template_id, data_json, style_json, title, pdf_url, created_at)
VALUES
(1, 1, '{"name":"Admin"}',  '{"color":"blue"}',   'CV Admin',  '/uploads/pdf/cv_admin.pdf', NOW()),
(2, 2, '{"name":"User"}',   '{"color":"black"}',  'CV User',   '/uploads/pdf/cv_user.pdf', NOW()),
(3, 3, '{"name":"Editor"}', '{"color":"orange"}', 'CV Editor', '/uploads/pdf/cv_editor.pdf', NOW());


-- ======================================
-- 6. FAVOURITE_CV
-- ======================================
CREATE TABLE favourite_cv (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, template_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES template(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO favourite_cv (user_id, template_id, created_at)
VALUES
(1, 1, NOW()),
(2, 1, NOW()),
(2, 3, NOW());


-- ======================================
-- 7. COMMENT (CÓ REPLY)
-- ======================================
CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    user_id BIGINT,
    content TEXT NOT NULL,
    parent_id BIGINT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES template(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO comment (template_id, user_id, content, parent_id, created_at)
VALUES
(1, 1, 'CV rất đẹp!', NULL, NOW()),
(2, 2, 'Minimal mà sang!', NULL, NOW()),
(3, 3, 'Phù hợp ngành Marketing!', NULL, NOW()),
(1, 2, 'Tôi đồng ý!', 1, NOW()); -- reply comment id=1


-- ======================================
-- 8. RATING
-- ======================================
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

INSERT INTO rating (template_id, user_id, score, created_at)
VALUES
(1, 1, 5, NOW()),
(2, 2, 4, NOW()),
(3, 3, 5, NOW());


-- ======================================
-- 9. HISTORY LOG
-- ======================================
CREATE TABLE history_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(100) NOT NULL,
    target_id BIGINT NOT NULL,
    description VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- ======================================
-- 10. AI_CHAT_HISTORY
-- ======================================
CREATE TABLE ai_chat_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sender ENUM('user', 'ai') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO ai_chat_history (user_id, message, sender, created_at)
VALUES
(1, 'Gợi ý cách cải thiện CV cho admin?', 'user', NOW()),
(1, 'Bạn có thể thêm phần kỹ năng quản lý và kinh nghiệm lãnh đạo.', 'ai', NOW()),
(2, 'Làm thế nào để CV IT nổi bật?', 'user', NOW()),
(2, 'Sử dụng template xanh chuyên nghiệp và nhấn mạnh dự án cá nhân.', 'ai', NOW());

-- ======================================
-- 11. ADMIN_CHAT_HISTORY
-- ======================================
CREATE TABLE admin_chat_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    admin_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sender ENUM('user', 'admin') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO admin_chat_history (user_id, admin_id, message, sender, created_at)
VALUES
(2, 1, 'Admin ơi, giúp tôi với template CV xanh.', 'user', NOW()),
(2, 1, 'Chào bạn, bạn cần chỉnh sửa phần nào?', 'admin', NOW()),
(3, 1, 'Tôi không thể tải PDF, lỗi gì vậy?', 'user', NOW()),
(3, 1, 'Kiểm tra lại kết nối, hoặc thử template khác.', 'admin', NOW());
-- ======================================
-- 12. CHAT_ROOM (WebSocket Chat Rooms)
-- ======================================
CREATE TABLE chat_room (
    id VARCHAR(100) PRIMARY KEY,                            
    room_type ENUM('user_admin', 'user_ai') NOT NULL,           
    status ENUM('pending', 'active', 'closed', 'timeout') NOT NULL DEFAULT 'pending',                     
    user_id BIGINT NOT NULL,                                
    admin_id BIGINT DEFAULT NULL,                           
    ai_enabled TINYINT(1) DEFAULT 0,                        
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_admin_id (admin_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ======================================
-- 13. CHAT_MESSAGE (WebSocket Messages)
-- ======================================
CREATE TABLE chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id VARCHAR(100) NOT NULL,
    sender_id BIGINT NULL,
    sender_type ENUM('user','admin','ai') NOT NULL DEFAULT 'user',
    content TEXT,
    type ENUM('text','image','video','audio','file') DEFAULT 'text',
    file_url VARCHAR(500),
    file_size BIGINT,
    file_mime VARCHAR(150),
    reply_to BIGINT NULL,
    status ENUM('sent','delivered','seen') DEFAULT 'sent',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_room(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user(id) ON DELETE SET NULL,
    FOREIGN KEY (reply_to) REFERENCES chat_message(id) ON DELETE SET NULL,
    INDEX idx_room_id (room_id),
    INDEX idx_sender_id (sender_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ======================================
-- 14. NOTIFICATION
-- ======================================
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receiver_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('chat_message', 'system', 'info') NOT NULL DEFAULT 'system',
    room_id VARCHAR(100) DEFAULT NULL,
    is_read TINYINT(1) DEFAULT 0,
    read_at DATETIME DEFAULT NULL,
    metadata JSON DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (receiver_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES chat_room(id) ON DELETE CASCADE,
    INDEX idx_receiver (receiver_id),
    INDEX idx_room_id (room_id),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




COMMIT;