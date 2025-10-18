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
    
    @Column(name = "nguoi_dung_id", nullable = false)
    private Long nguoiDungId;
    
    @Column(name = "loai_hoat_dong", nullable = false, length = 50)
    private String loaiHoatDong;
    
    @Column(name = "mo_ta", length = 500)
    private String moTa;
    
    @Column(name = "duong_dan", length = 500)
    private String duongDan;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
