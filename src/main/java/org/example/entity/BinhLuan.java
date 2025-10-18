package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "binh_luan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinhLuan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "bai_viet_id", nullable = false)
    private Long baiVietId;
    
    @Column(name = "nguoi_dung_id", nullable = false)
    private Long nguoiDungId;
    
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;
    
    @Column(name = "binh_luan_cha_id")
    private Long binhLuanChaId;
    
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
