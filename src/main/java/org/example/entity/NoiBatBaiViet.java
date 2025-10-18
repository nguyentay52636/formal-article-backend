package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "noi_bat_bai_viet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoiBatBaiViet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "bai_viet_id", nullable = false)
    private Long baiVietId;
    
    @Column(name = "khoi_noi_bat_id", nullable = false)
    private Long khoiNoiBatId;
    
    @Column(name = "vi_tri")
    private Integer viTri;
    
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
