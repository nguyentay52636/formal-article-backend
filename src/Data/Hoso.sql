SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
SET NAMES utf8mb4;

-- -----------------------------------------------------
-- 1. user
-- -----------------------------------------------------

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

-- -----------------------------------------------------
-- 2. file_upload
-- -----------------------------------------------------

CREATE TABLE file_upload (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type ENUM('image','document','other') NOT NULL,
    mime_type VARCHAR(100),
    file_name VARCHAR(255),
    path VARCHAR(500),
    size INT,
    width INT,
    height INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- 3. tag
-- -----------------------------------------------------

CREATE TABLE tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    slug VARCHAR(150) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- 4. template
-- -----------------------------------------------------

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

-- -----------------------------------------------------
-- 5. generated_cv
-- -----------------------------------------------------

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

-- -----------------------------------------------------
-- 6. favourite_cv
-- -----------------------------------------------------

CREATE TABLE favourite_cv (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, template_id),

    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES template(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- 7. comment
-- -----------------------------------------------------

CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_id BIGINT NOT NULL,
    user_id BIGINT,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (template_id) REFERENCES template(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- 8. rating
-- -----------------------------------------------------

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

<<<<<<< HEAD
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `donxinviec`
--
-- CREATE database hoso
-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bai_viet`
--

CREATE TABLE `bai_viet` (
  `id` bigint(20) NOT NULL,
  `danh_muc_id` bigint(20) NOT NULL,
  `tac_gia_id` bigint(20) NOT NULL,
  `duong_dan` varchar(180) NOT NULL,
  `tieu_de` varchar(220) NOT NULL,
  `tom_tat` varchar(300) DEFAULT NULL,
  `noi_dung_html` longtext DEFAULT NULL,
  `trang_thai` enum('nhap','xuat_ban','luu_tru') DEFAULT 'nhap',
  `ngay_xuat_ban` datetime DEFAULT NULL,
  `anh_dai_dien_id` bigint(20) DEFAULT NULL,
  `thong_tin_bo_sung` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`thong_tin_bo_sung`)),
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp(),
  `ngay_cap_nhat` datetime DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `bai_viet`
--

INSERT INTO `bai_viet` (`id`, `danh_muc_id`, `tac_gia_id`, `duong_dan`, `tieu_de`, `tom_tat`, `noi_dung_html`, `trang_thai`, `ngay_xuat_ban`, `anh_dai_dien_id`, `thong_tin_bo_sung`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, 1, 3, 'mau-don-xin-viec-2025', 'Mẫu đơn xin việc 2025', 'Tổng hợp các mẫu đơn xin việc chuyên nghiệp.', '<p>Nội dung bài viết HTML...</p>', 'xuat_ban', '2025-10-10 14:19:40', 1, NULL, '2025-10-10 14:19:40', NULL),
(2, 2, 3, 'kinh-nghiem-viet-cv', 'Kinh nghiệm viết CV gây ấn tượng', 'Chia sẻ cách viết CV chuẩn', '<p>Kinh nghiệm thực tế...</p>', 'xuat_ban', '2025-10-10 14:19:40', 1, NULL, '2025-10-10 14:19:40', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bai_viet_tep_tin`
--

CREATE TABLE `bai_viet_tep_tin` (
  `id` bigint(20) NOT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `tep_tin_id` bigint(20) NOT NULL,
  `loai_lien_ket` enum('anh_dai_dien','tep_dinh_kem') NOT NULL,
  `thu_tu` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `bai_viet_tep_tin`
--

INSERT INTO `bai_viet_tep_tin` (`id`, `bai_viet_id`, `tep_tin_id`, `loai_lien_ket`, `thu_tu`) VALUES
(1, 1, 1, 'anh_dai_dien', 0),
(2, 1, 2, 'tep_dinh_kem', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bai_viet_the`
--

CREATE TABLE `bai_viet_the` (
  `bai_viet_id` bigint(20) NOT NULL,
  `the_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `bai_viet_the`
--

INSERT INTO `bai_viet_the` (`bai_viet_id`, `the_id`) VALUES
(1, 3),
(2, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `binh_luan`
--

CREATE TABLE `binh_luan` (
  `id` bigint(20) NOT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `nguoi_dung_id` bigint(20) DEFAULT NULL,
  `ten_khach` varchar(120) DEFAULT NULL,
  `email_khach` varchar(160) DEFAULT NULL,
  `noi_dung` text NOT NULL,
  `trang_thai` enum('cho_duyet','da_duyet','spam','xoa') DEFAULT 'cho_duyet',
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `binh_luan`
--

INSERT INTO `binh_luan` (`id`, `bai_viet_id`, `nguoi_dung_id`, `ten_khach`, `email_khach`, `noi_dung`, `trang_thai`, `ngay_tao`) VALUES
(1, 1, 4, NULL, NULL, 'Bài viết rất hay!', 'da_duyet', '2025-10-10 14:19:40'),
(2, 1, NULL, 'Khách ẩn danh', 'guest@example.com', 'Tôi sẽ áp dụng mẫu đơn này.', 'da_duyet', '2025-10-10 14:19:40');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danh_muc`
--

CREATE TABLE `danh_muc` (
  `id` bigint(20) NOT NULL,
  `danh_muc_cha` bigint(20) DEFAULT NULL,
  `duong_dan` varchar(160) NOT NULL,
  `ten` varchar(160) NOT NULL,
  `mo_ta` text DEFAULT NULL,
  `thu_tu` int(11) NOT NULL DEFAULT 0,
  `kich_hoat` tinyint(1) NOT NULL DEFAULT 1,
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp(),
  `ngay_cap_nhat` datetime DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `danh_muc`
--

INSERT INTO `danh_muc` (`id`, `danh_muc_cha`, `duong_dan`, `ten`, `mo_ta`, `thu_tu`, `kich_hoat`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, NULL, 'tin-tuc', 'Tin tức', 'Tin tức tổng hợp', 1, 1, '2025-10-10 14:19:40', NULL),
(2, NULL, 'huong-dan', 'Hướng dẫn', 'Hướng dẫn sử dụng', 2, 1, '2025-10-10 14:19:40', NULL),
(3, 1, 'thoi-su', 'Thời sự', 'Tin thời sự trong nước', 3, 1, '2025-10-10 14:19:40', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khoi_noi_bat`
--

CREATE TABLE `khoi_noi_bat` (
  `id` bigint(20) NOT NULL,
  `ma` varchar(80) NOT NULL,
  `ten` varchar(160) NOT NULL,
  `cau_hinh` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`cau_hinh`)),
  `kich_hoat` tinyint(1) NOT NULL DEFAULT 1,
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp(),
  `ngay_cap_nhat` datetime DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `khoi_noi_bat`
--

INSERT INTO `khoi_noi_bat` (`id`, `ma`, `ten`, `cau_hinh`, `kich_hoat`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, 'trang-chu-1', 'Khối trang chủ 1', '{\"layout\":\"carousel\"}', 1, '2025-10-10 14:19:40', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `lich_su_hoat_dong`
--

CREATE TABLE `lich_su_hoat_dong` (
  `id` bigint(20) NOT NULL,
  `nguoi_thuc_hien_id` bigint(20) DEFAULT NULL,
  `doi_tuong` enum('bai_viet','danh_muc','tep_tin','binh_luan','noi_bat') NOT NULL,
  `doi_tuong_id` bigint(20) NOT NULL,
  `hanh_dong` varchar(60) NOT NULL,
  `truoc` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`truoc`)),
  `sau` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`sau`)),
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nguoi_dung`
--

CREATE TABLE `nguoi_dung` (
  `id` bigint(20) NOT NULL,
  `email` varchar(150) NOT NULL,
  `mat_khau` varchar(255) NOT NULL,
  `ho_ten` varchar(120) DEFAULT NULL,
  `vai_tro_id` tinyint(4) NOT NULL DEFAULT 3 COMMENT '1: quản trị, 2: biên tập, 3: tác giả, 4: độc giả',
  `kich_hoat` tinyint(1) NOT NULL DEFAULT 1,
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp(),
  `ngay_cap_nhat` datetime DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nguoi_dung`
--

INSERT INTO `nguoi_dung` (`id`, `email`, `mat_khau`, `ho_ten`, `vai_tro_id`, `kich_hoat`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, 'admin@example.com', 'pw_admin', 'Quản trị viên', 1, 1, '2025-10-10 14:19:40', '2025-10-10 15:01:15'),
(2, 'editor@example.com', 'pw_editor', 'Biên tập viên', 2, 1, '2025-10-10 14:19:40', '2025-10-10 15:01:15'),
(3, 'author@example.com', 'pw_author', 'Tác giả A', 3, 1, '2025-10-10 14:19:40', '2025-10-10 15:01:15'),
(4, 'reader@example.com', 'pw_reader', 'Độc giả B', 4, 1, '2025-10-10 14:19:40', '2025-10-10 15:01:15'),
(5, 'abc@gmail.com', '1', 'thanh', 1, 1, '2025-10-10 07:57:19', '2025-10-10 15:01:15'),
(6, 'A@gmail.com', '1', 'tay', 3, 1, '2025-10-10 08:11:26', '2025-10-10 08:11:26');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `noi_bat_bai_viet`
--

CREATE TABLE `noi_bat_bai_viet` (
  `id` bigint(20) NOT NULL,
  `khoi_id` bigint(20) NOT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `thu_tu` int(11) NOT NULL DEFAULT 0,
  `ghi_chu` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `noi_bat_bai_viet`
--

INSERT INTO `noi_bat_bai_viet` (`id`, `khoi_id`, `bai_viet_id`, `thu_tu`, `ghi_chu`) VALUES
(1, 1, 1, 1, 'Bài nổi bật đầu tiên'),
(2, 1, 2, 2, 'Bài nổi bật thứ hai');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phan_ung`
--

CREATE TABLE `phan_ung` (
  `id` bigint(20) NOT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `nguoi_dung_id` bigint(20) DEFAULT NULL,
  `loai` enum('thich','sao','huu_ich') NOT NULL,
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phan_ung`
--

INSERT INTO `phan_ung` (`id`, `bai_viet_id`, `nguoi_dung_id`, `loai`, `ngay_tao`) VALUES
(1, 1, 4, 'thich', '2025-10-10 14:19:40'),
(2, 1, 3, 'huu_ich', '2025-10-10 14:19:40');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tai_lieu_da_luu`
--

CREATE TABLE `tai_lieu_da_luu` (
  `id` bigint(20) NOT NULL,
  `nguoi_dung_id` bigint(20) NOT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `tep_tin_id` bigint(20) DEFAULT NULL,
  `ngay_luu` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `tai_lieu_da_luu`
--

INSERT INTO `tai_lieu_da_luu` (`id`, `nguoi_dung_id`, `bai_viet_id`, `tep_tin_id`, `ngay_luu`) VALUES
(1, 4, 1, 2, '2025-10-10 14:19:40');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tep_tin`
--

CREATE TABLE `tep_tin` (
  `id` bigint(20) NOT NULL,
  `nguoi_tai_id` bigint(20) NOT NULL,
  `loai` enum('anh','tai_lieu','khac') NOT NULL,
  `dinh_dang` varchar(100) NOT NULL,
  `ten_tap_tin` varchar(255) NOT NULL,
  `kich_thuoc` int(11) NOT NULL,
  `duong_dan_luu` varchar(500) NOT NULL,
  `chieu_rong` int(11) DEFAULT NULL,
  `chieu_cao` int(11) DEFAULT NULL,
  `ngay_tao` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `tep_tin`
--

INSERT INTO `tep_tin` (`id`, `nguoi_tai_id`, `loai`, `dinh_dang`, `ten_tap_tin`, `kich_thuoc`, `duong_dan_luu`, `chieu_rong`, `chieu_cao`, `ngay_tao`) VALUES
(1, 1, 'anh', 'image/jpeg', 'anh_dai_dien_1.jpg', 204800, '/uploads/anh_dai_dien_1.jpg', 800, 600, '2025-10-10 14:19:40'),
(2, 3, 'tai_lieu', 'application/pdf', 'cv_mau.pdf', 512000, '/uploads/cv_mau.pdf', NULL, NULL, '2025-10-10 14:19:40');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `the`
--

CREATE TABLE `the` (
  `id` bigint(20) NOT NULL,
  `duong_dan` varchar(160) NOT NULL,
  `ten` varchar(160) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `the`
--

INSERT INTO `the` (`id`, `duong_dan`, `ten`) VALUES
(1, 'viec-lam', 'Việc làm'),
(2, 'kinh-nghiem', 'Kinh nghiệm'),
(3, 'mau-don', 'Mẫu đơn');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thong_ke_bai_viet`
--

CREATE TABLE `thong_ke_bai_viet` (
  `bai_viet_id` bigint(20) NOT NULL,
  `luot_xem` bigint(20) NOT NULL DEFAULT 0,
  `luot_tai` bigint(20) NOT NULL DEFAULT 0,
  `so_binh_luan` bigint(20) NOT NULL DEFAULT 0,
  `lan_xem_cuoi` datetime DEFAULT NULL,
  `lan_tai_cuoi` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `thong_ke_bai_viet`
--

INSERT INTO `thong_ke_bai_viet` (`bai_viet_id`, `luot_xem`, `luot_tai`, `so_binh_luan`, `lan_xem_cuoi`, `lan_tai_cuoi`) VALUES
(1, 150, 25, 2, '2025-10-10 14:19:40', '2025-10-10 14:19:40'),
(2, 90, 5, 0, '2025-10-10 14:19:40', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vai_tro`
--

CREATE TABLE `vai_tro` (
  `id` tinyint(4) NOT NULL,
  `ma` varchar(50) NOT NULL,
  `ten` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `vai_tro`
--

INSERT INTO `vai_tro` (`id`, `ma`, `ten`) VALUES
(1, 'quan_tri', 'Quản trị viên'),
(2, 'bien_tap', 'Biên tập viên'),
(3, 'tac_gia', 'Tác giả'),
(4, 'doc_gia', 'Độc giả');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `bai_viet`
--
ALTER TABLE `bai_viet`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `duong_dan` (`duong_dan`),
  ADD KEY `tac_gia_id` (`tac_gia_id`),
  ADD KEY `idx_bai_viet_dm_tt` (`danh_muc_id`,`trang_thai`,`ngay_xuat_ban`);

--
-- Chỉ mục cho bảng `bai_viet_tep_tin`
--
ALTER TABLE `bai_viet_tep_tin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bai_viet_id` (`bai_viet_id`),
  ADD KEY `tep_tin_id` (`tep_tin_id`);

--
-- Chỉ mục cho bảng `bai_viet_the`
--
ALTER TABLE `bai_viet_the`
  ADD PRIMARY KEY (`bai_viet_id`,`the_id`),
  ADD KEY `the_id` (`the_id`);

--
-- Chỉ mục cho bảng `binh_luan`
--
ALTER TABLE `binh_luan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bai_viet_id` (`bai_viet_id`),
  ADD KEY `nguoi_dung_id` (`nguoi_dung_id`);

--
-- Chỉ mục cho bảng `danh_muc`
--
ALTER TABLE `danh_muc`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `duong_dan` (`duong_dan`),
  ADD KEY `danh_muc_cha` (`danh_muc_cha`);

--
-- Chỉ mục cho bảng `khoi_noi_bat`
--
ALTER TABLE `khoi_noi_bat`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma` (`ma`);

--
-- Chỉ mục cho bảng `lich_su_hoat_dong`
--
ALTER TABLE `lich_su_hoat_dong`
  ADD PRIMARY KEY (`id`),
  ADD KEY `nguoi_thuc_hien_id` (`nguoi_thuc_hien_id`);

--
-- Chỉ mục cho bảng `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_vai_tro` (`vai_tro_id`);

--
-- Chỉ mục cho bảng `noi_bat_bai_viet`
--
ALTER TABLE `noi_bat_bai_viet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `khoi_id` (`khoi_id`),
  ADD KEY `bai_viet_id` (`bai_viet_id`);

--
-- Chỉ mục cho bảng `phan_ung`
--
ALTER TABLE `phan_ung`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bai_viet_id` (`bai_viet_id`),
  ADD KEY `nguoi_dung_id` (`nguoi_dung_id`);

--
-- Chỉ mục cho bảng `tai_lieu_da_luu`
--
ALTER TABLE `tai_lieu_da_luu`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nguoi_dung_id` (`nguoi_dung_id`,`bai_viet_id`),
  ADD KEY `bai_viet_id` (`bai_viet_id`),
  ADD KEY `tep_tin_id` (`tep_tin_id`);

--
-- Chỉ mục cho bảng `tep_tin`
--
ALTER TABLE `tep_tin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `nguoi_tai_id` (`nguoi_tai_id`);

--
-- Chỉ mục cho bảng `the`
--
ALTER TABLE `the`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `duong_dan` (`duong_dan`);

--
-- Chỉ mục cho bảng `thong_ke_bai_viet`
--
ALTER TABLE `thong_ke_bai_viet`
  ADD PRIMARY KEY (`bai_viet_id`);

--
-- Chỉ mục cho bảng `vai_tro`
--
ALTER TABLE `vai_tro`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma` (`ma`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `bai_viet`
--
ALTER TABLE `bai_viet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `bai_viet_tep_tin`
--
ALTER TABLE `bai_viet_tep_tin`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `binh_luan`
--
ALTER TABLE `binh_luan`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `danh_muc`
--
ALTER TABLE `danh_muc`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `khoi_noi_bat`
--
ALTER TABLE `khoi_noi_bat`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `lich_su_hoat_dong`
--
ALTER TABLE `lich_su_hoat_dong`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `noi_bat_bai_viet`
--
ALTER TABLE `noi_bat_bai_viet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `phan_ung`
--
ALTER TABLE `phan_ung`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `tai_lieu_da_luu`
--
ALTER TABLE `tai_lieu_da_luu`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `tep_tin`
--
ALTER TABLE `tep_tin`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `the`
--
ALTER TABLE `the`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bai_viet`
--
ALTER TABLE `bai_viet`
  ADD CONSTRAINT `bai_viet_ibfk_1` FOREIGN KEY (`danh_muc_id`) REFERENCES `danh_muc` (`id`),
  ADD CONSTRAINT `bai_viet_ibfk_2` FOREIGN KEY (`tac_gia_id`) REFERENCES `nguoi_dung` (`id`);

--
-- Các ràng buộc cho bảng `bai_viet_tep_tin`
--
ALTER TABLE `bai_viet_tep_tin`
  ADD CONSTRAINT `bai_viet_tep_tin_ibfk_1` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `bai_viet_tep_tin_ibfk_2` FOREIGN KEY (`tep_tin_id`) REFERENCES `tep_tin` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `bai_viet_the`
--
ALTER TABLE `bai_viet_the`
  ADD CONSTRAINT `bai_viet_the_ibfk_1` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `bai_viet_the_ibfk_2` FOREIGN KEY (`the_id`) REFERENCES `the` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `binh_luan`
--
ALTER TABLE `binh_luan`
  ADD CONSTRAINT `binh_luan_ibfk_1` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `binh_luan_ibfk_2` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`id`);

--
-- Các ràng buộc cho bảng `danh_muc`
--
ALTER TABLE `danh_muc`
  ADD CONSTRAINT `danh_muc_ibfk_1` FOREIGN KEY (`danh_muc_cha`) REFERENCES `danh_muc` (`id`);

--
-- Các ràng buộc cho bảng `lich_su_hoat_dong`
--
ALTER TABLE `lich_su_hoat_dong`
  ADD CONSTRAINT `lich_su_hoat_dong_ibfk_1` FOREIGN KEY (`nguoi_thuc_hien_id`) REFERENCES `nguoi_dung` (`id`);

--
-- Các ràng buộc cho bảng `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  ADD CONSTRAINT `fk_vai_tro` FOREIGN KEY (`vai_tro_id`) REFERENCES `vai_tro` (`id`);

--
-- Các ràng buộc cho bảng `noi_bat_bai_viet`
--
ALTER TABLE `noi_bat_bai_viet`
  ADD CONSTRAINT `noi_bat_bai_viet_ibfk_1` FOREIGN KEY (`khoi_id`) REFERENCES `khoi_noi_bat` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `noi_bat_bai_viet_ibfk_2` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `phan_ung`
--
ALTER TABLE `phan_ung`
  ADD CONSTRAINT `phan_ung_ibfk_1` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `phan_ung_ibfk_2` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`id`);

--
-- Các ràng buộc cho bảng `tai_lieu_da_luu`
--
ALTER TABLE `tai_lieu_da_luu`
  ADD CONSTRAINT `tai_lieu_da_luu_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tai_lieu_da_luu_ibfk_2` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tai_lieu_da_luu_ibfk_3` FOREIGN KEY (`tep_tin_id`) REFERENCES `tep_tin` (`id`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `tep_tin`
--
ALTER TABLE `tep_tin`
  ADD CONSTRAINT `tep_tin_ibfk_1` FOREIGN KEY (`nguoi_tai_id`) REFERENCES `nguoi_dung` (`id`);

--
-- Các ràng buộc cho bảng `thong_ke_bai_viet`
--
ALTER TABLE `thong_ke_bai_viet`
  ADD CONSTRAINT `thong_ke_bai_viet_ibfk_1` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`) ON DELETE CASCADE;
=======
>>>>>>> acb48ad (update :restore)
COMMIT;
