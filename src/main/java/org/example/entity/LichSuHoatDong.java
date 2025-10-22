package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_hoat_dong")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichSuHoatDong {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nguoi_thuc_hien_id", nullable = false)
    private Long nguoiThucHienId;
    
    @Column(name = "doi_tuong", nullable = false, length = 100)
    private String doiTuong;
    
    @Column(name = "doi_tuong_id")
    private Long doiTuongId;
    
    @Column(name = "hanh_dong", nullable = false, length = 100)
    private String hanhDong;
    
    @Column(name = "truoc", columnDefinition = "LONGTEXT")
    private String truoc;
    
    @Column(name = "sau", columnDefinition = "LONGTEXT")
    private String sau;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
