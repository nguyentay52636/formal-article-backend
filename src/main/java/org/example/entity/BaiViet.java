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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bai_viet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"danhMuc", "tacGia", "binhLuans", "phanUngs", "thes", "tepTins", "taiLieuDaLuus"})
@ToString(exclude = {"danhMuc", "tacGia", "binhLuans", "phanUngs", "thes", "tepTins", "taiLieuDaLuus"})
public class BaiViet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_id", nullable = false)
    private DanhMuc danhMuc;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tac_gia_id", nullable = false)
    private NguoiDung tacGia;
    
    @Column(name = "duong_dan", nullable = false, length = 180, unique = true)
    private String duongDan;
    
    @Column(name = "tieu_de", nullable = false, length = 220)
    private String tieuDe;
    
    @Column(name = "tom_tat", length = 300)
    private String tomTat;
    
    @Column(name = "noi_dung_html", columnDefinition = "LONGTEXT")
    private String noiDungHtml;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.NHAP;
    
    @Column(name = "ngay_xuat_ban")
    private LocalDateTime ngayXuatBan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anh_dai_dien_id")
    private TepTin anhDaiDien;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    // Relationships
    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BinhLuan> binhLuans = new ArrayList<>();
    
    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhanUng> phanUngs = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "bai_viet_the",
        joinColumns = @JoinColumn(name = "bai_viet_id"),
        inverseJoinColumns = @JoinColumn(name = "the_id")
    )
    private Set<The> thes = new HashSet<>();
    
    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BaiVietTepTin> tepTins = new ArrayList<>();
    
    // @OneToOne(mappedBy = "baiViet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private ThongKeBaiViet thongKe;
    
    @OneToMany(mappedBy = "baiViet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaiLieuDaLuu> taiLieuDaLuus = new ArrayList<>();
    
    public enum TrangThai {
        NHAP, XUAT_BAN, LUU_TRU
    }
}
