package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nguoi_dung")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"vaiTro", "baiViets", "binhLuans", "phanUngs", "tepTins", "taiLieuDaLuus"})
@ToString(exclude = {"vaiTro", "baiViets", "binhLuans", "phanUngs", "tepTins", "taiLieuDaLuus"})
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anh_dai_dien_id")
    private TepTin anhDaiDien;
    
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
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vai_tro_id")
    private VaiTro vaiTro;
    
    @OneToMany(mappedBy = "tacGia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BaiViet> baiViets = new ArrayList<>();
    
    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BinhLuan> binhLuans = new ArrayList<>();
    
    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanUng> phanUngs = new ArrayList<>();
    
    @OneToMany(mappedBy = "nguoiTao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TepTin> tepTins = new ArrayList<>();
    
    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaiLieuDaLuu> taiLieuDaLuus = new ArrayList<>();
}
