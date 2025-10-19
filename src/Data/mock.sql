SET FOREIGN_KEY_CHECKS=0;

-- 1. Vai trò
INSERT INTO vai_tro (id, ma, ten) VALUES
(1, 'quan_tri', 'Quản trị viên'),
(2, 'bien_tap', 'Biên tập viên'),
(3, 'tac_gia', 'Tác giả'),
(4, 'doc_gia', 'Độc giả');

-- 2. Người dùng
INSERT INTO nguoi_dung (id, email, mat_khau, ho_ten, vai_tro_id, kich_hoat)
VALUES
(1, 'admin@example.com', 'pw_admin', 'Quản trị viên', 1, 1),
(2, 'editor@example.com', 'pw_editor', 'Biên tập viên', 2, 1),
(3, 'author@example.com', 'pw_author', 'Tác giả A', 3, 1),
(4, 'reader@example.com', 'pw_reader', 'Độc giả B', 4, 1),
(5, 'abc@gmail.com', '1', 'thanh', 1, 1),
(6, 'A@gmail.com', '1', 'tay', 3, 1);

-- 3. Danh mục
INSERT INTO danh_muc (id, danh_muc_cha, duong_dan, ten, mo_ta, thu_tu)
VALUES
(1, NULL, 'tin-tuc', 'Tin tức', 'Tin tức tổng hợp', 1),
(2, NULL, 'huong-dan', 'Hướng dẫn', 'Hướng dẫn sử dụng', 2),
(3, 1, 'thoi-su', 'Thời sự', 'Tin thời sự trong nước', 3);

-- 4. Thẻ
INSERT INTO the (id, duong_dan, ten) VALUES
(1, 'viec-lam', 'Việc làm'),
(2, 'kinh-nghiem', 'Kinh nghiệm'),
(3, 'mau-don', 'Mẫu đơn');

-- 5. Tệp tin
INSERT INTO tep_tin (id, nguoi_tai_id, loai, dinh_dang, ten_tap_tin, kich_thuoc, duong_dan_luu, chieu_rong, chieu_cao)
VALUES
(1, 1, 'anh', 'image/jpeg', 'anh_dai_dien_1.jpg', 204800, '/uploads/anh_dai_dien_1.jpg', 800, 600),
(2, 3, 'tai_lieu', 'application/pdf', 'cv_mau.pdf', 512000, '/uploads/cv_mau.pdf', NULL, NULL);

-- 6. Bài viết
INSERT INTO bai_viet (id, danh_muc_id, tac_gia_id, duong_dan, tieu_de, tom_tat, noi_dung_html, trang_thai, ngay_xuat_ban, anh_dai_dien_id)
VALUES
(1, 1, 3, 'mau-don-xin-viec-2025', 'Mẫu đơn xin việc 2025', 'Tổng hợp các mẫu đơn xin việc chuyên nghiệp.', '<p>Nội dung bài viết HTML...</p>', 'xuat_ban', '2025-10-10 14:19:40', 1),
(2, 2, 3, 'kinh-nghiem-viet-cv', 'Kinh nghiệm viết CV gây ấn tượng', 'Chia sẻ cách viết CV chuẩn', '<p>Kinh nghiệm thực tế...</p>', 'xuat_ban', '2025-10-10 14:19:40', 1);

-- 7. Bài viết - Thẻ
INSERT INTO bai_viet_the (bai_viet_id, the_id) VALUES
(1, 3),
(2, 2);

-- 8. Bài viết - Tệp tin
INSERT INTO bai_viet_tep_tin (id, bai_viet_id, tep_tin_id, loai_lien_ket, thu_tu)
VALUES
(1, 1, 1, 'anh_dai_dien', 0),
(2, 1, 2, 'tep_dinh_kem', 1);

-- 9. Bình luận
INSERT INTO binh_luan (id, bai_viet_id, nguoi_dung_id, ten_khach, email_khach, noi_dung, trang_thai)
VALUES
(1, 1, 4, NULL, NULL, 'Bài viết rất hay!', 'da_duyet'),
(2, 1, NULL, 'Khách ẩn danh', 'guest@example.com', 'Tôi sẽ áp dụng mẫu đơn này.', 'da_duyet');

-- 10. Phản ứng
INSERT INTO phan_ung (id, bai_viet_id, nguoi_dung_id, loai)
VALUES
(1, 1, 4, 'thich'),
(2, 1, 3, 'huu_ich');

-- 11. Tài liệu đã lưu
INSERT INTO tai_lieu_da_luu (id, nguoi_dung_id, bai_viet_id, tep_tin_id)
VALUES
(1, 4, 1, 2);

-- 12. Khối nổi bật
INSERT INTO khoi_noi_bat (id, ma, ten, cau_hinh, kich_hoat)
VALUES
(1, 'trang-chu-1', 'Khối trang chủ 1', '{"layout":"carousel"}', 1);

-- 13. Nổi bật - Bài viết
INSERT INTO noi_bat_bai_viet (id, khoi_id, bai_viet_id, thu_tu, ghi_chu)
VALUES
(1, 1, 1, 1, 'Bài nổi bật đầu tiên'),
(2, 1, 2, 2, 'Bài nổi bật thứ hai');

-- 14. Thống kê bài viết
INSERT INTO thong_ke_bai_viet (bai_viet_id, luot_xem, luot_tai, so_binh_luan, lan_xem_cuoi, lan_tai_cuoi)
VALUES
(1, 150, 25, 2, '2025-10-10 14:19:40', '2025-10-10 14:19:40'),
(2, 90, 5, 0, '2025-10-10 14:19:40', NULL);

SET FOREIGN_KEY_CHECKS=1;
COMMIT;
