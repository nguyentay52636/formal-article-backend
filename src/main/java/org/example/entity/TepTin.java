package org.example.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
@Table(name = "tep_tin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"nguoiTao", "baiVietTepTins", "anhDaiDiens"})
@ToString(exclude = {"nguoiTao", "baiVietTepTins", "anhDaiDiens"})
public class TepTin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ten", nullable = false, length = 255)
    private String ten;
    
    @Column(name = "duong_dan", nullable = false, length = 500)
    private String duongDan;
    
    @Column(name = "loai", nullable = false)
    private String loaiTep;
    
    @Column(name = "dinh_dang", nullable = false, length = 100)
    private String dinhDang;
    
    @Column(name = "kich_thuoc", nullable = false)
    private Long kichThuoc;
    
    @Column(name = "duong_dan_luu", nullable = false, length = 500)
    private String duongDanLuu;
    
    @Column(name = "chieu_rong")
    private Integer chieuRong;
    
    @Column(name = "chieu_cao")
    private Integer chieuCao;
    
    @Column(name = "mo_ta", length = 500)
    private String moTa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tai_id", nullable = false)
    private NguoiDung nguoiTao;
    
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
    @OneToMany(mappedBy = "tepTin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BaiVietTepTin> baiVietTepTins = new ArrayList<>();
    
    @OneToMany(mappedBy = "anhDaiDien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BaiViet> anhDaiDiens = new ArrayList<>();
    
    public enum LoaiTep {
        anh, tai_lieu, khac
    }
}
