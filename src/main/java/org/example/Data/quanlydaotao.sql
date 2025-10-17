-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1

-- Thời gian đã tạo: Th5 20, 2025 lúc 01:08 PM

-- Thời gian đã tạo: Th5 20, 2025 lúc 10:11 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlydaotao`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_cotdiem`
--

CREATE TABLE `ctdt_cotdiem` (
  `idCotDiem` int(11) NOT NULL,
  `maSV` varchar(50) NOT NULL,
  `tenSV` varchar(255) NOT NULL,
  `diemChuyenCan` float NOT NULL,
  `diemThucHanh` float NOT NULL,
  `diemGiuaKy` float NOT NULL,
  `diemCuoiKy` float NOT NULL,
  `bangDiemMon` varchar(255) NOT NULL,
  `hocKy` int(10) NOT NULL,
  `nam` varchar(50) NOT NULL,
  `lop` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_cotdiem`
--

INSERT INTO `ctdt_cotdiem` (`idCotDiem`, `maSV`, `tenSV`, `diemChuyenCan`, `diemThucHanh`, `diemGiuaKy`, `diemCuoiKy`, `bangDiemMon`, `hocKy`, `nam`, `lop`) VALUES
(6, 'SV001', 'Nguyễn Văn An', 9, 8, 7, 8, 'hp1', 1, '2023', 'DHKTPM17A'),
(7, 'SV002', 'Trần Thị Bình', 10, 9, 8, 9, 'hp2', 1, '2023', 'DHKTPM17A'),
(8, 'SV003', 'Lê Thị Cẩm', 8, 7, 7, 8, 'hp3', 2, '2023', 'DHKTPM17A'),
(9, 'SV004', 'Phạm Văn Duy', 9, 8, 9, 9, 'hp4', 2, '2023', 'DHKTPM17A'),
(10, 'SV005', 'Đoàn Thị Em', 10, 10, 9, 10, 'hp5', 3, '2023', 'DHKTPM17A');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_decuongchitiet`
--

CREATE TABLE `ctdt_decuongchitiet` (
  `id` int(11) NOT NULL,
  `mucTieu` text DEFAULT NULL,
  `idHocPhan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_decuongchitiet`
--

INSERT INTO `ctdt_decuongchitiet` (`id`, `mucTieu`, `idHocPhan`) VALUES
(36, 'Nắm được kiến thức về toán logic', 1),
(37, 'Viết được chương trình đơn giản bằng C', 2),
(38, 'Thiết kế, truy vấn CSDL quan hệ', 3),
(39, 'Hiểu cơ chế mạng máy tính', 4),
(40, 'Phân tích hệ thống thông tin', 5);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_giangvien`
--

CREATE TABLE `ctdt_giangvien` (
  `idGiangVien` int(11) NOT NULL,
  `idTaiKhoan` int(11) DEFAULT NULL,
  `maGiangVien` varchar(50) NOT NULL,
  `tenGiangVien` varchar(150) NOT NULL,
  `chucDanh` varchar(150) NOT NULL,
  `namPhong` varchar(50) NOT NULL,
  `trinhDo` varchar(50) NOT NULL,
  `nuoc` varchar(150) NOT NULL,
  `namTotNghiep` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_giangvien`
--

INSERT INTO `ctdt_giangvien` (`idGiangVien`, `idTaiKhoan`, `maGiangVien`, `tenGiangVien`, `chucDanh`, `namPhong`, `trinhDo`, `nuoc`, `namTotNghiep`) VALUES
(1, 1, 'GV001', 'Trần Thị B', 'PGS.TS', '2015', 'Tiến sĩ', 'Việt Nam', '2012'),
(2, 2, 'GV002', 'Lê Thị C', 'TS', '2017', 'Thạc sĩ', 'Việt Nam', '2015'),
(3, 3, 'GV003', 'Nguyễn Văn A', 'ThS', '2018', 'Thạc sĩ', 'Anh', '2014'),
(4, 4, 'GV004', 'Phạm Văn D', 'TS', '2020', 'Tiến sĩ', 'Mỹ', '2016');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_hocky`
--

CREATE TABLE `ctdt_hocky` (
  `idHocKy` int(11) NOT NULL,
  `tenHocKy` varchar(255) NOT NULL,
  `idHocPhan` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`idHocPhan`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_hocky`
--

INSERT INTO `ctdt_hocky` (`idHocKy`, `tenHocKy`, `idHocPhan`) VALUES
(1, 'Học kỳ 1', '[1,2,3]'),
(2, 'Học kỳ 2', '[3,4,5]'),
(3, 'Học kỳ 3', '[2,4,5]'),
(4, 'Học kỳ 4', '[2,4,5]'),
(5, 'Học kỳ 5', '[1,2,4]');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_hocphan`
--

CREATE TABLE `ctdt_hocphan` (
  `idHocPhan` int(11) NOT NULL,
  `maHP` varchar(150) NOT NULL,
  `tenHP` varchar(150) NOT NULL,
  `soTinChi` int(50) NOT NULL,
  `soTietLyThuyet` int(50) NOT NULL,
  `soTietThucHanh` int(50) NOT NULL,
  `soTietThucTap` int(50) NOT NULL,
  `loaiHocPhan` int(50) NOT NULL,
  `tongSoTiet` int(50) NOT NULL,
  `heSoHocPhan` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_hocphan`
--

INSERT INTO `ctdt_hocphan` (`idHocPhan`, `maHP`, `tenHP`, `soTinChi`, `soTietLyThuyet`, `soTietThucHanh`, `soTietThucTap`, `loaiHocPhan`, `tongSoTiet`, `heSoHocPhan`) VALUES
(1, 'HP001', 'Toán rời rạc', 3, 30, 15, 0, 0, 45, 1),
(2, 'HP002', 'Lập trình C', 4, 30, 30, 0, 0, 60, 1),
(3, 'HP003', 'CSDL', 3, 30, 15, 0, 0, 45, 1),
(4, 'HP004', 'Mạng máy tính', 3, 25, 20, 0, 0, 45, 1),
(5, 'HP005', 'PTTKHT', 4, 40, 20, 0, 0, 60, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_kehoachdayhoc`
--

CREATE TABLE `ctdt_kehoachdayhoc` (
  `idChuyenNganh` int(11) NOT NULL,
  `tenChuyenNganh` varchar(150) NOT NULL,
  `idHocKy` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`idHocKy`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_kehoachdayhoc`
--

INSERT INTO `ctdt_kehoachdayhoc` (`idChuyenNganh`, `tenChuyenNganh`, `idHocKy`) VALUES
(1, 'Tên chuyên ngành mới', '[1,2,3,4]'),
(2, 'Khoa học máy tính', '[2,3,4]'),
(3, 'Khoa học máy tính', '[3,4,5]'),
(4, 'CNTT', '[2,3]'),
(5, 'Công nghệ phần mềm', '[3,5]'),
(7, 'Tên chuyên ngành', '[1,2,3,4,5]');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_kehoachmonhom`
--

CREATE TABLE `ctdt_kehoachmonhom` (
  `id` int(11) NOT NULL,
  `namHoc` varchar(255) NOT NULL,
  `soNhom` int(11) NOT NULL,
  `idHocPhan` int(11) NOT NULL,
  `hocKy` int(11) NOT NULL,
  `soLuongSinhVien` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_kehoachmonhom`
--

INSERT INTO `ctdt_kehoachmonhom` (`id`, `namHoc`, `soNhom`, `idHocPhan`, `hocKy`, `soLuongSinhVien`) VALUES
(1, '2023-2024', 3, 1, 1, 45),
(2, '2023-2024', 2, 2, 1, 30),
(3, '2023-2024', 2, 3, 2, 30),
(4, '2023-2024', 1, 4, 2, 45),
(5, '2023-2024', 2, 5, 3, 30);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_khoikienthuc`
--

CREATE TABLE `ctdt_khoikienthuc` (
  `idKhoiKienThuc` int(11) NOT NULL,
  `tenKhoiKienThuc` varchar(150) NOT NULL,
  `idKienThuc` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_khoikienthuc`
--

INSERT INTO `ctdt_khoikienthuc` (`idKhoiKienThuc`, `tenKhoiKienThuc`, `idKienThuc`) VALUES
(21, 'Khối kiến thức cơ sở (Cập nhật)', '[1,2,3,4]'),
(22, 'Lập trình cơ bản', '[2, 3, 4]'),
(23, 'Cơ sở dữ liệu', '[3, 4, 5]'),
(24, 'Mạng máy tính', '[4, 5, 1]'),
(25, 'Khối kiến thức cơ sở (Cập nhật)', '[1,2,3,4]');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_khungchuongtrinh`
--

CREATE TABLE `ctdt_khungchuongtrinh` (
  `id` int(11) NOT NULL,
  `idThongTin` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_khungchuongtrinh`
--

INSERT INTO `ctdt_khungchuongtrinh` (`id`, `idThongTin`) VALUES
(1, 1),
(2, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_kienthuc`
--

CREATE TABLE `ctdt_kienthuc` (
  `idKienThuc` int(11) NOT NULL,
  `tenKienThuc` varchar(200) NOT NULL,
  `idHocPhan` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`idHocPhan`)),
  `loaiHocPhan` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_kienthuc`
--

INSERT INTO `ctdt_kienthuc` (`idKienThuc`, `tenKienThuc`, `idHocPhan`, `loaiHocPhan`) VALUES
(1, 'Toán rời rạc', '[1, 2]', '0'),
(2, 'Lập trình C', '[2, 3]', '0'),
(3, 'CSDL', '[3, 4]', '0'),
(4, 'Mạng máy tính', '[4, 5]', '0'),
(5, 'Phân tích thiết kế hệ thống', '[5, 1]', '0');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_phanconggiangday`
--

CREATE TABLE `ctdt_phanconggiangday` (
  `idPhanCong` int(11) NOT NULL,
  `idGiangVien` int(11) NOT NULL,
  `idHocPhan` int(11) NOT NULL,
  `tenGiangVien` varchar(11) NOT NULL,
  `hocKy` int(11) NOT NULL,
  `tenMonHoc` varchar(150) NOT NULL,
  `soTietThucHien` int(11) NOT NULL,
  `soTietThucTe` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_phanconggiangday`
--

INSERT INTO `ctdt_phanconggiangday` (`idPhanCong`, `idGiangVien`, `idHocPhan`, `tenGiangVien`, `hocKy`, `tenMonHoc`, `soTietThucHien`, `soTietThucTe`) VALUES
(31, 1, 1, 'Trần Thị B', 1, 'Toán rời rạc', 45, 45),
(32, 2, 2, 'Lê Thị C', 1, 'Lập trình C', 60, 60),
(33, 3, 3, 'Nguyễn Văn ', 2, 'CSDL', 45, 45),
(34, 4, 4, 'Phạm Văn D', 2, 'Mạng máy tính', 45, 44);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_thongtinchung`
--

CREATE TABLE `ctdt_thongtinchung` (
  `id` int(11) NOT NULL,
  `tenChuongTrinh` varchar(50) NOT NULL,
  `bac` varchar(50) NOT NULL,
  `loaiBang` varchar(50) NOT NULL,
  `loaiHinhDaoTao` varchar(50) NOT NULL,
  `thoiGian` varchar(50) NOT NULL,
  `soTinChi` int(30) NOT NULL,
  `khoaQuanLy` varchar(50) NOT NULL,
  `ngonNgu` varchar(50) NOT NULL,
  `khoaTuyen` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_thongtinchung`
--

INSERT INTO `ctdt_thongtinchung` (`id`, `tenChuongTrinh`, `bac`, `loaiBang`, `loaiHinhDaoTao`, `thoiGian`, `soTinChi`, `khoaQuanLy`, `ngonNgu`, `khoaTuyen`) VALUES
(1, 'Kỹ thuật phần mềm', 'Đại học', 'Cử nhân', 'Chính quy', '4 năm', 130, 'CNTT', 'Tiếng Việt', '2021'),
(2, 'Hệ thống thông tin', 'Đại học', 'Cử nhân', 'Chính quy', '4 năm', 130, 'CNTT', 'Tiếng Việt', '2021'),
(3, 'An toàn thông tin', 'Đại học', 'Cử nhân', 'Chính quy', '4 năm', 130, 'CNTT', 'Tiếng Việt', '2022');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctdt_user`
--

CREATE TABLE `ctdt_user` (
  `id` int(11) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `userEmail` varchar(150) NOT NULL,
  `password` varchar(150) NOT NULL,
  `role` int(11) NOT NULL COMMENT '0:User, 1: Giang vien, 2: Admin'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ctdt_user`
--

INSERT INTO `ctdt_user` (`id`, `userName`, `userEmail`, `password`, `role`) VALUES
(1, 'nguyenvana', 'vana@example.com', 'hashedpwd1', 0),
(2, 'tranthib', 'thib@example.com', 'hashedpwd2', 0),
(3, 'lethic', 'thic@example.com', 'hashedpwd3', 0),
(4, 'phamvand', 'vand@example.com', 'hashedpwd4', 0);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `ctdt_cotdiem`
--
ALTER TABLE `ctdt_cotdiem`
  ADD PRIMARY KEY (`idCotDiem`);

--
-- Chỉ mục cho bảng `ctdt_decuongchitiet`
--
ALTER TABLE `ctdt_decuongchitiet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_ctdt_decuongchitiet_ctdt_hocphan` (`idHocPhan`);

--
-- Chỉ mục cho bảng `ctdt_giangvien`
--
ALTER TABLE `ctdt_giangvien`
  ADD PRIMARY KEY (`idGiangVien`),
  ADD KEY `FK_ctdt_giangvien_ctdt_user` (`idTaiKhoan`);

--
-- Chỉ mục cho bảng `ctdt_hocky`
--
ALTER TABLE `ctdt_hocky`
  ADD PRIMARY KEY (`idHocKy`);

--
-- Chỉ mục cho bảng `ctdt_hocphan`
--
ALTER TABLE `ctdt_hocphan`
  ADD PRIMARY KEY (`idHocPhan`);

--
-- Chỉ mục cho bảng `ctdt_kehoachdayhoc`
--
ALTER TABLE `ctdt_kehoachdayhoc`
  ADD PRIMARY KEY (`idChuyenNganh`);

--
-- Chỉ mục cho bảng `ctdt_kehoachmonhom`
--
ALTER TABLE `ctdt_kehoachmonhom`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `ctdt_khoikienthuc`
--
ALTER TABLE `ctdt_khoikienthuc`
  ADD PRIMARY KEY (`idKhoiKienThuc`);

--
-- Chỉ mục cho bảng `ctdt_khungchuongtrinh`
--
ALTER TABLE `ctdt_khungchuongtrinh`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_ctdt_khungchuongtrinh_ctdt_thongtinchung` (`idThongTin`);

--
-- Chỉ mục cho bảng `ctdt_kienthuc`
--
ALTER TABLE `ctdt_kienthuc`
  ADD PRIMARY KEY (`idKienThuc`);

--
-- Chỉ mục cho bảng `ctdt_phanconggiangday`
--
ALTER TABLE `ctdt_phanconggiangday`
  ADD PRIMARY KEY (`idPhanCong`),
  ADD KEY `FK_ctdt_phanconggiangday_ctdt_hocphan` (`idHocPhan`),
  ADD KEY `FK_ctdt_phanconggiangday_ctdt_giangvien` (`idGiangVien`);

--
-- Chỉ mục cho bảng `ctdt_thongtinchung`
--
ALTER TABLE `ctdt_thongtinchung`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `ctdt_user`
--
ALTER TABLE `ctdt_user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `ctdt_cotdiem`
--
ALTER TABLE `ctdt_cotdiem`
  MODIFY `idCotDiem` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `ctdt_decuongchitiet`
--
ALTER TABLE `ctdt_decuongchitiet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT cho bảng `ctdt_giangvien`
--
ALTER TABLE `ctdt_giangvien`
  MODIFY `idGiangVien` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT cho bảng `ctdt_hocky`
--
ALTER TABLE `ctdt_hocky`
  MODIFY `idHocKy` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `ctdt_hocphan`
--
ALTER TABLE `ctdt_hocphan`
  MODIFY `idHocPhan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `ctdt_kehoachdayhoc`
--
ALTER TABLE `ctdt_kehoachdayhoc`
  MODIFY `idChuyenNganh` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `ctdt_kehoachmonhom`
--
ALTER TABLE `ctdt_kehoachmonhom`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `ctdt_khoikienthuc`
--
ALTER TABLE `ctdt_khoikienthuc`
  MODIFY `idKhoiKienThuc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT cho bảng `ctdt_khungchuongtrinh`
--
ALTER TABLE `ctdt_khungchuongtrinh`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `ctdt_kienthuc`
--
ALTER TABLE `ctdt_kienthuc`
  MODIFY `idKienThuc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT cho bảng `ctdt_phanconggiangday`
--
ALTER TABLE `ctdt_phanconggiangday`
  MODIFY `idPhanCong` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT cho bảng `ctdt_thongtinchung`
--
ALTER TABLE `ctdt_thongtinchung`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `ctdt_user`
--
ALTER TABLE `ctdt_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `ctdt_decuongchitiet`
--
ALTER TABLE `ctdt_decuongchitiet`
  ADD CONSTRAINT `FK_ctdt_decuongchitiet_ctdt_hocphan` FOREIGN KEY (`idHocPhan`) REFERENCES `ctdt_hocphan` (`idHocPhan`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `ctdt_giangvien`
--
ALTER TABLE `ctdt_giangvien`
  ADD CONSTRAINT `FK_ctdt_giangvien_ctdt_user` FOREIGN KEY (`idTaiKhoan`) REFERENCES `ctdt_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `ctdt_khungchuongtrinh`
--
ALTER TABLE `ctdt_khungchuongtrinh`
  ADD CONSTRAINT `FK_ctdt_khungchuongtrinh_ctdt_thongtinchung` FOREIGN KEY (`idThongTin`) REFERENCES `ctdt_thongtinchung` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `ctdt_phanconggiangday`
--
ALTER TABLE `ctdt_phanconggiangday`
  ADD CONSTRAINT `FK_ctdt_phanconggiangday_ctdt_giangvien` FOREIGN KEY (`idGiangVien`) REFERENCES `ctdt_giangvien` (`idGiangVien`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_ctdt_phanconggiangday_ctdt_hocphan` FOREIGN KEY (`idHocPhan`) REFERENCES `ctdt_hocphan` (`idHocPhan`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `ctdt_kehoachmonhom`
--
ALTER TABLE `ctdt_kehoachmonhom`
  ADD CONSTRAINT `FK_ctdt_kehoachmonhom_ctdt_hocphan` FOREIGN KEY (`idHocPhan`) REFERENCES `ctdt_hocphan` (`idHocPhan`) ON DELETE NO ACTION ON UPDATE NO ACTION;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
