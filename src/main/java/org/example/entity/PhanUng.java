package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "phan_ung")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhanUng {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "bai_viet_id", nullable = false)
    private Long baiVietId;
    
    @Column(name = "nguoi_dung_id", nullable = false)
    private Long nguoiDungId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_phan_ung", nullable = false)
    private LoaiPhanUng loaiPhanUng;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    public enum LoaiPhanUng {
        THICH, KHONG_THICH, YEU_THICH, CHIA_SE
    }
}
