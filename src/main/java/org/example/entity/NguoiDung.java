package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "nguoi_dung")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDung {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;
    
    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;
    
    @Column(name = "anh_dai_dien_id")
    private Long anhDaiDienId;
    
    @Column(name = "gioi_tinh")
    private String gioiTinh;
    
    @Column(name = "ngay_sinh")
    private LocalDateTime ngaySinh;
    
    @Column(name = "dia_chi", length = 500)
    private String diaChi;
    
    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;
    
    @Column(name = "trang_thai")
    private String trangThai;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
