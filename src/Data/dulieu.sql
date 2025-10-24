-- File dữ liệu mẫu cho database article-backend
-- Thực hiện các INSERT statements theo thứ tự từ trên xuống dưới

-- 1. Bảng vai_tro (bảng cha)
INSERT IGNORE INTO `vai_tro` (`id`, `ma`, `ten`) VALUES
(1, 'quan_tri', 'Quản trị viên'),
(2, 'bien_tap', 'Biên tập viên'),
(3, 'tac_gia', 'Tác giả'),
(4, 'doc_gia', 'Độc giả'),
(5, 'moderator', 'Điều hành viên');

-- 2. Bảng danh_muc (bảng độc lập)
INSERT IGNORE INTO `danh_muc` (`id`, `danh_muc_cha`, `duong_dan`, `ten`, `mo_ta`, `thu_tu`, `kich_hoat`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, NULL, 'tin-tuc', 'Tin tức', 'Tin tức tổng hợp về các lĩnh vực', 1, 1, '2025-01-01 10:00:00', NULL),
(2, NULL, 'huong-dan', 'Hướng dẫn', 'Hướng dẫn sử dụng và thực hành', 2, 1, '2025-01-01 10:00:00', NULL),
(3, 1, 'thoi-su', 'Thời sự', 'Tin thời sự trong nước và quốc tế', 3, 1, '2025-01-01 10:00:00', NULL),
(4, 2, 'ky-nang', 'Kỹ năng', 'Hướng dẫn phát triển kỹ năng', 4, 1, '2025-01-01 10:00:00', NULL),
(5, NULL, 'cong-nghe', 'Công nghệ', 'Tin tức và xu hướng công nghệ', 5, 1, '2025-01-01 10:00:00', NULL);

-- 3. Bảng the (bảng độc lập)
INSERT IGNORE INTO `the` (`id`, `duong_dan`, `ten`) VALUES
(1, 'viec-lam', 'Việc làm'),
(2, 'kinh-nghiem', 'Kinh nghiệm'),
(3, 'mau-don', 'Mẫu đơn'),
(4, 'ky-nang-mem', 'Kỹ năng mềm'),
(5, 'cong-nghe-moi', 'Công nghệ mới');

-- 4. Bảng nguoi_dung (phụ thuộc vào vai_tro)
INSERT IGNORE INTO `nguoi_dung` (`id`, `email`, `mat_khau`, `ho_ten`, `vai_tro_id`, `kich_hoat`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Nguyễn Văn Admin', 1, 1, '2025-01-01 10:00:00', NULL),
(2, 'editor@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Trần Thị Biên Tập', 2, 1, '2025-01-01 10:00:00', NULL),
(3, 'author@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Lê Văn Tác Giả', 3, 1, '2025-01-01 10:00:00', NULL),
(4, 'reader@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Phạm Thị Độc Giả', 4, 1, '2025-01-01 10:00:00', NULL),
(5, 'mod@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Hoàng Văn Moderator', 5, 1, '2025-01-01 10:00:00', NULL);

-- 5. Bảng bai_viet (phụ thuộc vào danh_muc và nguoi_dung)
INSERT IGNORE INTO `bai_viet` (`id`, `danh_muc_id`, `tac_gia_id`, `duong_dan`, `tieu_de`, `tom_tat`, `noi_dung_html`, `trang_thai`, `ngay_xuat_ban`, `anh_dai_dien_id`, `thong_tin_bo_sung`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, 1, 3, 'mau-don-xin-viec-2025', 'Mẫu đơn xin việc chuyên nghiệp 2025', 'Tổng hợp các mẫu đơn xin việc chuyên nghiệp và hiệu quả nhất.', '<h1>Mẫu đơn xin việc 2025</h1><p>Nội dung chi tiết về các mẫu đơn xin việc...</p>', 'xuat_ban', '2025-01-01 10:00:00', 1, '{"tags":["cv","xin-viec"],"priority":"high"}', '2025-01-01 10:00:00', NULL),
(2, 2, 2, 'kinh-nghiem-viet-cv', 'Kinh nghiệm viết CV gây ấn tượng', 'Chia sẻ cách viết CV chuẩn và thu hút nhà tuyển dụng.', '<h1>Kinh nghiệm viết CV</h1><p>Hướng dẫn chi tiết cách viết CV...</p>', 'xuat_ban', '2025-01-01 10:00:00', 2, '{"tags":["cv","ky-nang"],"priority":"medium"}', '2025-01-01 10:00:00', NULL),
(3, 3, 3, 'tin-tuc-thoi-su-moi-nhat', 'Tin tức thời sự mới nhất', 'Cập nhật tin tức thời sự trong nước và quốc tế.', '<h1>Tin tức thời sự</h1><p>Các tin tức mới nhất...</p>', 'xuat_ban', '2025-01-01 10:00:00', 3, '{"tags":["thoi-su","tin-tuc"],"priority":"high"}', '2025-01-01 10:00:00', NULL),
(4, 4, 2, 'ky-nang-giao-tiep', 'Kỹ năng giao tiếp hiệu quả', 'Hướng dẫn phát triển kỹ năng giao tiếp trong công việc.', '<h1>Kỹ năng giao tiếp</h1><p>Phương pháp cải thiện kỹ năng giao tiếp...</p>', 'xuat_ban', '2025-01-01 10:00:00', 4, '{"tags":["ky-nang","giao-tiep"],"priority":"medium"}', '2025-01-01 10:00:00', NULL),
(5, 5, 3, 'cong-nghe-ai-2025', 'Công nghệ AI và xu hướng 2025', 'Tổng quan về công nghệ AI và xu hướng phát triển.', '<h1>Công nghệ AI 2025</h1><p>Xu hướng và ứng dụng của AI...</p>', 'xuat_ban', '2025-01-01 10:00:00', 5, '{"tags":["ai","cong-nghe"],"priority":"high"}', '2025-01-01 10:00:00', NULL);

-- 6. Bảng tep_tin (phụ thuộc vào nguoi_dung)
INSERT IGNORE INTO `tep_tin` (`id`, `nguoi_tai_id`, `loai`, `dinh_dang`, `ten_tap_tin`, `kich_thuoc`, `duong_dan_luu`, `chieu_rong`, `chieu_cao`, `ngay_tao`) VALUES
(1, 1, 'anh', 'image/jpeg', 'anh_dai_dien_1.jpg', 204800, '/uploads/anh_dai_dien_1.jpg', 800, 600, '2025-01-01 10:00:00'),
(2, 2, 'tai_lieu', 'application/pdf', 'cv_mau.pdf', 512000, '/uploads/cv_mau.pdf', NULL, NULL, '2025-01-01 10:00:00'),
(3, 3, 'anh', 'image/png', 'banner_thoi_su.png', 153600, '/uploads/banner_thoi_su.png', 1200, 400, '2025-01-01 10:00:00'),
(4, 2, 'tai_lieu', 'application/pdf', 'huong_dan_ky_nang.pdf', 768000, '/uploads/huong_dan_ky_nang.pdf', NULL, NULL, '2025-01-01 10:00:00'),
(5, 3, 'anh', 'image/jpeg', 'cong_nghe_ai.jpg', 307200, '/uploads/cong_nghe_ai.jpg', 1000, 500, '2025-01-01 10:00:00');

-- 7. Bảng bai_viet_tep_tin (phụ thuộc vào bai_viet và tep_tin)
INSERT IGNORE INTO `bai_viet_tep_tin` (`id`, `bai_viet_id`, `tep_tin_id`, `loai_lien_ket`, `thu_tu`) VALUES
(1, 1, 1, 'anh_dai_dien', 0),
(2, 1, 2, 'tep_dinh_kem', 1),
(3, 2, 2, 'tep_dinh_kem', 0),
(4, 3, 3, 'anh_dai_dien', 0),
(5, 4, 4, 'tep_dinh_kem', 0),
(6, 5, 5, 'anh_dai_dien', 0);

-- 8. Bảng bai_viet_the (phụ thuộc vào bai_viet và the)
INSERT IGNORE INTO `bai_viet_the` (`bai_viet_id`, `the_id`) VALUES
(1, 1),
(1, 3),
(2, 2),
(2, 4),
(3, 1),
(4, 4),
(5, 5);

-- 9. Bảng binh_luan (phụ thuộc vào bai_viet và nguoi_dung)
INSERT IGNORE INTO `binh_luan` (`id`, `bai_viet_id`, `nguoi_dung_id`, `ten_khach`, `email_khach`, `noi_dung`, `trang_thai`, `ngay_tao`) VALUES
(1, 1, 4, NULL, NULL, 'Bài viết rất hay và hữu ích!', 'da_duyet', '2025-01-01 11:00:00'),
(2, 1, NULL, 'Khách ẩn danh', 'guest@example.com', 'Tôi sẽ áp dụng mẫu đơn này ngay.', 'da_duyet', '2025-01-01 11:30:00'),
(3, 2, 5, NULL, NULL, 'Cảm ơn tác giả đã chia sẻ kinh nghiệm quý báu.', 'da_duyet', '2025-01-01 12:00:00'),
(4, 3, NULL, 'Người đọc', 'reader@example.com', 'Tin tức cập nhật rất nhanh.', 'da_duyet', '2025-01-01 12:30:00'),
(5, 4, 4, NULL, NULL, 'Kỹ năng giao tiếp rất quan trọng trong công việc.', 'da_duyet', '2025-01-01 13:00:00');

-- 10. Bảng phan_ung (phụ thuộc vào bai_viet và nguoi_dung)
INSERT IGNORE INTO `phan_ung` (`id`, `bai_viet_id`, `nguoi_dung_id`, `loai`, `ngay_tao`) VALUES
(1, 1, 4, 'thich', '2025-01-01 11:00:00'),
(2, 1, 3, 'huu_ich', '2025-01-01 11:15:00'),
(3, 2, 5, 'thich', '2025-01-01 12:00:00'),
(4, 3, 4, 'sao', '2025-01-01 12:30:00'),
(5, 4, 5, 'huu_ich', '2025-01-01 13:00:00');

-- 11. Bảng tai_lieu_da_luu (phụ thuộc vào nguoi_dung, bai_viet, tep_tin)
INSERT IGNORE INTO `tai_lieu_da_luu` (`id`, `nguoi_dung_id`, `bai_viet_id`, `tep_tin_id`, `ngay_luu`) VALUES
(1, 4, 1, 2, '2025-01-01 11:00:00'),
(2, 5, 2, 2, '2025-01-01 12:00:00'),
(3, 4, 4, 4, '2025-01-01 13:00:00'),
(4, 5, 1, NULL, '2025-01-01 11:30:00'),
(5, 4, 5, NULL, '2025-01-01 13:30:00');

-- 12. Bảng thong_ke_bai_viet (phụ thuộc vào bai_viet)
INSERT IGNORE INTO `thong_ke_bai_viet` (`bai_viet_id`, `luot_xem`, `luot_tai`, `so_binh_luan`, `lan_xem_cuoi`, `lan_tai_cuoi`) VALUES
(1, 150, 25, 2, '2025-01-01 14:00:00', '2025-01-01 13:30:00'),
(2, 90, 5, 1, '2025-01-01 13:45:00', '2025-01-01 12:30:00'),
(3, 200, 10, 1, '2025-01-01 14:15:00', '2025-01-01 13:00:00'),
(4, 75, 15, 1, '2025-01-01 13:30:00', '2025-01-01 13:15:00'),
(5, 300, 8, 0, '2025-01-01 14:30:00', NULL);

-- 13. Bảng khoi_noi_bat (bảng độc lập)
INSERT IGNORE INTO `khoi_noi_bat` (`id`, `ma`, `ten`, `cau_hinh`, `kich_hoat`, `ngay_tao`, `ngay_cap_nhat`) VALUES
(1, 'trang-chu-1', 'Khối trang chủ 1', '{"layout":"carousel","autoPlay":true}', 1, '2025-01-01 10:00:00', NULL),
(2, 'sidebar-1', 'Khối sidebar 1', '{"layout":"list","showImage":true}', 1, '2025-01-01 10:00:00', NULL),
(3, 'footer-1', 'Khối footer 1', '{"layout":"grid","columns":3}', 1, '2025-01-01 10:00:00', NULL),
(4, 'banner-1', 'Khối banner 1', '{"layout":"banner","height":"300px"}', 1, '2025-01-01 10:00:00', NULL),
(5, 'featured-1', 'Khối nổi bật 1', '{"layout":"featured","limit":5}', 1, '2025-01-01 10:00:00', NULL);

-- 14. Bảng noi_bat_bai_viet (phụ thuộc vào khoi_noi_bat và bai_viet)
INSERT IGNORE INTO `noi_bat_bai_viet` (`id`, `khoi_id`, `bai_viet_id`, `thu_tu`, `ghi_chu`) VALUES
(1, 1, 1, 1, 'Bài nổi bật đầu tiên trên trang chủ'),
(2, 1, 2, 2, 'Bài nổi bật thứ hai trên trang chủ'),
(3, 2, 3, 1, 'Bài nổi bật trong sidebar'),
(4, 4, 5, 1, 'Banner công nghệ AI'),
(5, 5, 1, 1, 'Bài viết được đề xuất');

-- 15. Bảng lich_su_hoat_dong (phụ thuộc vào nguoi_dung)
INSERT IGNORE INTO `lich_su_hoat_dong` (`id`, `nguoi_thuc_hien_id`, `doi_tuong`, `doi_tuong_id`, `hanh_dong`, `truoc`, `sau`, `ngay_tao`) VALUES
(1, 3, 'bai_viet', 1, 'tao_moi', NULL, '{"tieu_de":"Mẫu đơn xin việc chuyên nghiệp 2025","trang_thai":"nhap"}', '2025-01-01 10:00:00'),
(2, 2, 'bai_viet', 1, 'xuat_ban', '{"trang_thai":"nhap"}', '{"trang_thai":"xuat_ban","ngay_xuat_ban":"2025-01-01 10:00:00"}', '2025-01-01 10:00:00'),
(3, 3, 'bai_viet', 2, 'tao_moi', NULL, '{"tieu_de":"Kinh nghiệm viết CV gây ấn tượng","trang_thai":"nhap"}', '2025-01-01 10:00:00'),
(4, 2, 'bai_viet', 2, 'xuat_ban', '{"trang_thai":"nhap"}', '{"trang_thai":"xuat_ban","ngay_xuat_ban":"2025-01-01 10:00:00"}', '2025-01-01 10:00:00'),
(5, 1, 'danh_muc', 5, 'tao_moi', NULL, '{"ten":"Công nghệ","duong_dan":"cong-nghe"}', '2025-01-01 10:00:00');

-- Hoàn thành việc thêm dữ liệu mẫu cho tất cả các bảng
