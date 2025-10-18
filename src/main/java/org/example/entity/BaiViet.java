package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bai_viet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaiViet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "danh_muc_id", nullable = false)
    private Long danhMucId;
    
    @Column(name = "tac_gia_id", nullable = false)
    private Long tacGiaId;
    
    @Column(name = "duong_dan", nullable = false, length = 180)
    private String duongDan;
    
    @Column(name = "tieu_de", nullable = false, length = 220)
    private String tieuDe;
    
    @Column(name = "tom_tat", length = 300)
    private String tomTat;
    
    @Column(name = "noi_dung_html", columnDefinition = "LONGTEXT")
    private String noiDungHtml;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.NHAP;
    
    @Column(name = "ngay_xuat_ban")
    private LocalDateTime ngayXuatBan;
    
    @Column(name = "anh_dai_dien_id")
    private Long anhDaiDienId;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    public enum TrangThai {
        NHAP, XUAT_BAN, LUU_TRU
    }
}
