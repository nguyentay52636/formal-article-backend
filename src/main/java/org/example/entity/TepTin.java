package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tep_tin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TepTin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ten", nullable = false, length = 255)
    private String ten;
    
    @Column(name = "duong_dan", nullable = false, length = 500)
    private String duongDan;
    
    @Column(name = "loai_tep", length = 50)
    private String loaiTep;
    
    @Column(name = "kich_thuoc")
    private Long kichThuoc;
    
    @Column(name = "mo_ta", length = 500)
    private String moTa;
    
    @Column(name = "nguoi_tao_id")
    private Long nguoiTaoId;
    
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
