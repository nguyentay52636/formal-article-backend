-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th10 24, 2025 lúc 08:53 AM
-- Phiên bản máy phục vụ: 10.4.28-MariaDB
-- Phiên bản PHP: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `hoso`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bai_viet`
--

CREATE TABLE `bai_viet` (
  `id` bigint(20) NOT NULL,
  `duong_dan` varchar(180) NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `ngay_xuat_ban` datetime(6) DEFAULT NULL,
  `noi_dung_html` longtext DEFAULT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `tieu_de` varchar(220) NOT NULL,
  `tom_tat` varchar(300) DEFAULT NULL,
  `trang_thai` tinyint(4) DEFAULT NULL,
  `anh_dai_dien_id` bigint(20) DEFAULT NULL,
  `danh_muc_id` bigint(20) NOT NULL,
  `tac_gia_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bai_viet_tep_tin`
--

CREATE TABLE `bai_viet_tep_tin` (
  `id` bigint(20) NOT NULL,
  `loai_lien_ket` enum('ANH_DAI_DIEN','TEP_DINH_KEM') NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `thu_tu` int(11) NOT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `tep_tin_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bai_viet_the`
--

CREATE TABLE `bai_viet_the` (
  `bai_viet_id` bigint(20) NOT NULL,
  `the_id` bigint(20) NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `binh_luan`
--

CREATE TABLE `binh_luan` (
  `id` bigint(20) NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `noi_dung` text NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `trang_thai` varchar(255) DEFAULT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `binh_luan_cha_id` bigint(20) DEFAULT NULL,
  `nguoi_dung_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danh_muc`
--

CREATE TABLE `danh_muc` (
  `id` bigint(20) NOT NULL,
  `danh_muc_cha` bigint(20) DEFAULT NULL,
  `duong_dan` varchar(200) DEFAULT NULL,
  `kich_hoat` bit(1) NOT NULL,
  `mo_ta` varchar(500) DEFAULT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `ten` varchar(100) NOT NULL,
  `thu_tu` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khoi_noi_bat`
--

CREATE TABLE `khoi_noi_bat` (
  `id` bigint(20) NOT NULL,
  `mo_ta` varchar(500) DEFAULT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `ten` varchar(100) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `trang_thai` varchar(255) DEFAULT NULL,
  `vi_tri` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nguoi_dung`
--

CREATE TABLE `nguoi_dung` (
  `id` bigint(20) NOT NULL,
  `dia_chi` varchar(500) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `gioi_tinh` varchar(255) DEFAULT NULL,
  `ho_ten` varchar(100) NOT NULL,
  `mat_khau` varchar(255) NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_sinh` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `so_dien_thoai` varchar(20) DEFAULT NULL,
  `ten_dang_nhap` varchar(50) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `trang_thai` varchar(255) DEFAULT NULL,
  `anh_dai_dien_id` bigint(20) DEFAULT NULL,
  `vai_tro_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nguoi_dung`
--

INSERT INTO `nguoi_dung` (`id`, `dia_chi`, `email`, `gioi_tinh`, `ho_ten`, `mat_khau`, `ngay_cap_nhat`, `ngay_sinh`, `ngay_tao`, `so_dien_thoai`, `ten_dang_nhap`, `thong_tin_bo_sung`, `trang_thai`, `anh_dai_dien_id`, `vai_tro_id`) VALUES
(1, 'Hà Nội, Việt Nam', 'admin@hoso.com', 'Nam', 'Nguyễn Văn Admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 10:30:00.000000', '1985-03-15 00:00:00.000000', '2025-01-01 08:00:00.000000', '0123456789', 'admin', '{\"bio\": \"Quản trị viên hệ thống\", \"skills\": [\"management\", \"system_admin\"]}', 'active', NULL, 1),
(2, 'TP.HCM, Việt Nam', 'superadmin@hoso.com', 'Nữ', 'Trần Thị Super', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 11:00:00.000000', '1980-05-20 00:00:00.000000', '2025-01-01 08:00:00.000000', '0987654321', 'superadmin', '{\"bio\": \"Super admin với quyền cao nhất\", \"experience\": \"10+ years\"}', 'active', NULL, 1),
(3, 'Đà Nẵng, Việt Nam', 'editor1@hoso.com', 'Nam', 'Lê Văn Biên Tập', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 12:00:00.000000', '1988-07-10 00:00:00.000000', '2025-01-02 09:00:00.000000', '0123456788', 'editor1', '{\"bio\": \"Biên tập viên chuyên nghiệp\", \"specialization\": \"content_editing\"}', 'active', NULL, 2),
(4, 'Cần Thơ, Việt Nam', 'editor2@hoso.com', 'Nữ', 'Phạm Thị Chỉnh Sửa', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 13:00:00.000000', '1990-12-25 00:00:00.000000', '2025-01-02 09:30:00.000000', '0987654322', 'editor2', '{\"bio\": \"Chuyên gia chỉnh sửa nội dung\", \"languages\": [\"vi\", \"en\"]}', 'active', NULL, 2),
(5, 'Hải Phòng, Việt Nam', 'author1@hoso.com', 'Nam', 'Hoàng Văn Viết', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 14:00:00.000000', '1992-04-18 00:00:00.000000', '2025-01-03 10:00:00.000000', '0123456787', 'author1', '{\"bio\": \"Tác giả chuyên về công nghệ\", \"topics\": [\"tech\", \"programming\", \"career\"]}', 'active', NULL, 3),
(6, 'Nha Trang, Việt Nam', 'author2@hoso.com', 'Nữ', 'Vũ Thị Sáng Tạo', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 15:00:00.000000', '1995-08-30 00:00:00.000000', '2025-01-03 10:30:00.000000', '0987654323', 'author2', '{\"bio\": \"Chuyên gia viết về kinh doanh\", \"expertise\": [\"business\", \"marketing\", \"finance\"]}', 'active', NULL, 3),
(7, 'Huế, Việt Nam', 'author3@hoso.com', 'Nam', 'Đặng Văn Chuyên Nghiệp', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 16:00:00.000000', '1987-11-12 00:00:00.000000', '2025-01-04 11:00:00.000000', '0123456786', 'author3', '{\"bio\": \"Tác giả về giáo dục và phát triển\", \"focus\": [\"education\", \"personal_development\"]}', 'active', NULL, 3),
(8, 'Vũng Tàu, Việt Nam', 'reader1@hoso.com', 'Nữ', 'Ngô Thị Đọc Giả', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 17:00:00.000000', '1998-02-14 00:00:00.000000', '2025-01-05 12:00:00.000000', '0987654324', 'reader1', '{\"bio\": \"Độc giả tích cực\", \"interests\": [\"career\", \"learning\"]}', 'active', NULL, 4),
(9, 'Quy Nhon, Việt Nam', 'reader2@hoso.com', 'Nam', 'Bùi Văn Học Hỏi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 18:00:00.000000', '1996-06-08 00:00:00.000000', '2025-01-05 12:30:00.000000', '0123456785', 'reader2', '{\"bio\": \"Sinh viên tìm kiếm cơ hội việc làm\", \"status\": \"student\"}', 'active', NULL, 4),
(10, 'Buôn Ma Thuột, Việt Nam', 'reader3@hoso.com', 'Nữ', 'Lý Thị Tìm Việc', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 19:00:00.000000', '1994-09-22 00:00:00.000000', '2025-01-06 13:00:00.000000', '0987654325', 'reader3', '{\"bio\": \"Người tìm việc có kinh nghiệm\", \"experience\": \"3+ years\"}', 'active', NULL, 4),
(11, 'Pleiku, Việt Nam', 'reader4@hoso.com', 'Nam', 'Chu Văn Nghiên Cứu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 20:00:00.000000', '1991-01-05 00:00:00.000000', '2025-01-06 13:30:00.000000', '0123456784', 'reader4', '{\"bio\": \"Nghiên cứu viên quan tâm đến xu hướng\", \"field\": \"research\"}', 'active', NULL, 4),
(12, 'Long Xuyên, Việt Nam', 'reader5@hoso.com', 'Nữ', 'Hồ Thị Chia Sẻ', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '2025-01-15 21:00:00.000000', '1993-03-17 00:00:00.000000', '2025-01-07 14:00:00.000000', '0987654326', 'reader5', '{\"bio\": \"Thích chia sẻ kiến thức\", \"personality\": \"helpful\"}', 'active', NULL, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `noi_bat_bai_viet`
--

CREATE TABLE `noi_bat_bai_viet` (
  `id` bigint(20) NOT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `khoi_noi_bat_id` bigint(20) NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `trang_thai` varchar(255) DEFAULT NULL,
  `vi_tri` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `password_reset_codes`
--

CREATE TABLE `password_reset_codes` (
  `id` bigint(20) NOT NULL,
  `createdAt` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `expiresAt` datetime(6) NOT NULL,
  `resetCode` varchar(6) NOT NULL,
  `used` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phan_ung`
--

CREATE TABLE `phan_ung` (
  `id` bigint(20) NOT NULL,
  `loai` enum('THICH','SAO','HUU_ICH') NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `nguoi_dung_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tai_lieu_da_luu`
--

CREATE TABLE `tai_lieu_da_luu` (
  `id` bigint(20) NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_luu` datetime(6) NOT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `bai_viet_id` bigint(20) NOT NULL,
  `nguoi_dung_id` bigint(20) NOT NULL,
  `tep_tin_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tep_tin`
--

CREATE TABLE `tep_tin` (
  `id` bigint(20) NOT NULL,
  `chieu_cao` int(11) DEFAULT NULL,
  `chieu_rong` int(11) DEFAULT NULL,
  `dinh_dang` varchar(100) NOT NULL,
  `duong_dan` varchar(500) NOT NULL,
  `duong_dan_luu` varchar(500) NOT NULL,
  `kich_thuoc` bigint(20) NOT NULL,
  `loai` varchar(255) NOT NULL,
  `mo_ta` varchar(500) DEFAULT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `ten` varchar(255) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `trang_thai` varchar(255) DEFAULT NULL,
  `nguoi_tai_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `the`
--

CREATE TABLE `the` (
  `id` bigint(20) NOT NULL,
  `duong_dan` varchar(160) NOT NULL,
  `mau_sac` varchar(20) DEFAULT NULL,
  `mo_ta` varchar(500) DEFAULT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `ten` varchar(160) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `trang_thai` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thong_ke_bai_viet`
--

CREATE TABLE `thong_ke_bai_viet` (
  `id` bigint(20) NOT NULL,
  `lan_tai_cuoi` datetime(6) DEFAULT NULL,
  `lan_xem_cuoi` datetime(6) DEFAULT NULL,
  `luot_tai` bigint(20) NOT NULL,
  `luot_xem` bigint(20) NOT NULL,
  `ngay_cap_nhat` datetime(6) DEFAULT NULL,
  `ngay_tao` datetime(6) NOT NULL,
  `so_binh_luan` bigint(20) NOT NULL,
  `thong_tin_bo_sung` longtext DEFAULT NULL,
  `bai_viet_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vai_tro`
--

CREATE TABLE `vai_tro` (
  `id` bigint(20) NOT NULL,
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
  ADD UNIQUE KEY `UK_3y2cvk1wu6k7955spw49e128y` (`duong_dan`),
  ADD KEY `FK5bvmyxeh76cn0lcj3oyt4tanw` (`anh_dai_dien_id`),
  ADD KEY `FKd10wasymvmoea8oqctn648esn` (`danh_muc_id`),
  ADD KEY `FK3lijney913nclv6mes54kx10q` (`tac_gia_id`);

--
-- Chỉ mục cho bảng `bai_viet_tep_tin`
--
ALTER TABLE `bai_viet_tep_tin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7vd78jwmjd2jko9wae8tk0tpo` (`bai_viet_id`),
  ADD KEY `FKn5lp1f8unscslfvf2tc7x0ek6` (`tep_tin_id`);

--
-- Chỉ mục cho bảng `bai_viet_the`
--
ALTER TABLE `bai_viet_the`
  ADD PRIMARY KEY (`bai_viet_id`,`the_id`),
  ADD KEY `FKk8e8e3t1k9m8dy0n9iv3a4975` (`the_id`);

--
-- Chỉ mục cho bảng `binh_luan`
--
ALTER TABLE `binh_luan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbv4cktvi77gv9rv5cbdunl5cf` (`bai_viet_id`),
  ADD KEY `FK4ycge7pdyc3nvgr4oim951frk` (`binh_luan_cha_id`),
  ADD KEY `FK9t033bi499huul59bresxl05o` (`nguoi_dung_id`);

--
-- Chỉ mục cho bảng `danh_muc`
--
ALTER TABLE `danh_muc`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `khoi_noi_bat`
--
ALTER TABLE `khoi_noi_bat`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_majqh5g4djy2tp3p9dvr64brp` (`email`),
  ADD UNIQUE KEY `UK_o0s268lrp9is6o1e4ek6m1lc6` (`ten_dang_nhap`),
  ADD KEY `FKsh64al4h4m2d4d6sumfsb1yyg` (`anh_dai_dien_id`),
  ADD KEY `FKa5oibkto18llfdid5w4mv4v47` (`vai_tro_id`);

--
-- Chỉ mục cho bảng `noi_bat_bai_viet`
--
ALTER TABLE `noi_bat_bai_viet`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `password_reset_codes`
--
ALTER TABLE `password_reset_codes`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `phan_ung`
--
ALTER TABLE `phan_ung`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9c7wq20nh7d5gq3s95cm8csy6` (`bai_viet_id`),
  ADD KEY `FKf0bwij2fhqtmuin8s2rklty0g` (`nguoi_dung_id`);

--
-- Chỉ mục cho bảng `tai_lieu_da_luu`
--
ALTER TABLE `tai_lieu_da_luu`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrpcdot5p9e9y7bfeyrxkjbexq` (`bai_viet_id`),
  ADD KEY `FKcvao0vr7e18ids0if31ybbrrp` (`nguoi_dung_id`),
  ADD KEY `FKq7s2l46u44j9dxauw3utd84bw` (`tep_tin_id`);

--
-- Chỉ mục cho bảng `tep_tin`
--
ALTER TABLE `tep_tin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdsibcasyp99ft52jkw30sif9n` (`nguoi_tai_id`);

--
-- Chỉ mục cho bảng `the`
--
ALTER TABLE `the`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `thong_ke_bai_viet`
--
ALTER TABLE `thong_ke_bai_viet`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_h9mpbcui3db71e4m4i8b3myv` (`bai_viet_id`);

--
-- Chỉ mục cho bảng `vai_tro`
--
ALTER TABLE `vai_tro`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_qtouw8sgbc012dxjqi37r9qxq` (`ma`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `bai_viet`
--
ALTER TABLE `bai_viet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `bai_viet_tep_tin`
--
ALTER TABLE `bai_viet_tep_tin`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `binh_luan`
--
ALTER TABLE `binh_luan`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `danh_muc`
--
ALTER TABLE `danh_muc`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `khoi_noi_bat`
--
ALTER TABLE `khoi_noi_bat`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `noi_bat_bai_viet`
--
ALTER TABLE `noi_bat_bai_viet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `password_reset_codes`
--
ALTER TABLE `password_reset_codes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `phan_ung`
--
ALTER TABLE `phan_ung`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `tai_lieu_da_luu`
--
ALTER TABLE `tai_lieu_da_luu`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `tep_tin`
--
ALTER TABLE `tep_tin`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `the`
--
ALTER TABLE `the`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `thong_ke_bai_viet`
--
ALTER TABLE `thong_ke_bai_viet`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `vai_tro`
--
ALTER TABLE `vai_tro`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bai_viet`
--
ALTER TABLE `bai_viet`
  ADD CONSTRAINT `FK3lijney913nclv6mes54kx10q` FOREIGN KEY (`tac_gia_id`) REFERENCES `nguoi_dung` (`id`),
  ADD CONSTRAINT `FK5bvmyxeh76cn0lcj3oyt4tanw` FOREIGN KEY (`anh_dai_dien_id`) REFERENCES `tep_tin` (`id`),
  ADD CONSTRAINT `FKd10wasymvmoea8oqctn648esn` FOREIGN KEY (`danh_muc_id`) REFERENCES `danh_muc` (`id`);

--
-- Các ràng buộc cho bảng `bai_viet_tep_tin`
--
ALTER TABLE `bai_viet_tep_tin`
  ADD CONSTRAINT `FK7vd78jwmjd2jko9wae8tk0tpo` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`),
  ADD CONSTRAINT `FKn5lp1f8unscslfvf2tc7x0ek6` FOREIGN KEY (`tep_tin_id`) REFERENCES `tep_tin` (`id`);

--
-- Các ràng buộc cho bảng `bai_viet_the`
--
ALTER TABLE `bai_viet_the`
  ADD CONSTRAINT `FKhcs10s26b7r52h7mijj2mu5yq` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`),
  ADD CONSTRAINT `FKk8e8e3t1k9m8dy0n9iv3a4975` FOREIGN KEY (`the_id`) REFERENCES `the` (`id`);

--
-- Các ràng buộc cho bảng `binh_luan`
--
ALTER TABLE `binh_luan`
  ADD CONSTRAINT `FK4ycge7pdyc3nvgr4oim951frk` FOREIGN KEY (`binh_luan_cha_id`) REFERENCES `binh_luan` (`id`),
  ADD CONSTRAINT `FK9t033bi499huul59bresxl05o` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`id`),
  ADD CONSTRAINT `FKbv4cktvi77gv9rv5cbdunl5cf` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`);

--
-- Các ràng buộc cho bảng `nguoi_dung`
--
ALTER TABLE `nguoi_dung`
  ADD CONSTRAINT `FKa5oibkto18llfdid5w4mv4v47` FOREIGN KEY (`vai_tro_id`) REFERENCES `vai_tro` (`id`),
  ADD CONSTRAINT `FKsh64al4h4m2d4d6sumfsb1yyg` FOREIGN KEY (`anh_dai_dien_id`) REFERENCES `tep_tin` (`id`);

--
-- Các ràng buộc cho bảng `phan_ung`
--
ALTER TABLE `phan_ung`
  ADD CONSTRAINT `FK9c7wq20nh7d5gq3s95cm8csy6` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`),
  ADD CONSTRAINT `FKf0bwij2fhqtmuin8s2rklty0g` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`id`);

--
-- Các ràng buộc cho bảng `tai_lieu_da_luu`
--
ALTER TABLE `tai_lieu_da_luu`
  ADD CONSTRAINT `FKcvao0vr7e18ids0if31ybbrrp` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`id`),
  ADD CONSTRAINT `FKq7s2l46u44j9dxauw3utd84bw` FOREIGN KEY (`tep_tin_id`) REFERENCES `tep_tin` (`id`),
  ADD CONSTRAINT `FKrpcdot5p9e9y7bfeyrxkjbexq` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`);

--
-- Các ràng buộc cho bảng `tep_tin`
--
ALTER TABLE `tep_tin`
  ADD CONSTRAINT `FKdsibcasyp99ft52jkw30sif9n` FOREIGN KEY (`nguoi_tai_id`) REFERENCES `nguoi_dung` (`id`);

--
-- Các ràng buộc cho bảng `thong_ke_bai_viet`
--
ALTER TABLE `thong_ke_bai_viet`
  ADD CONSTRAINT `FKr4qu0ythj8k6jypi9g6m71uom` FOREIGN KEY (`bai_viet_id`) REFERENCES `bai_viet` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
