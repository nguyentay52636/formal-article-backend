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
@Table(name = "binh_luan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"baiViet", "nguoiDung", "binhLuanCha", "binhLuanCon"})
@ToString(exclude = {"baiViet", "nguoiDung", "binhLuanCha", "binhLuanCon"})
public class BinhLuan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bai_viet_id", nullable = false)
    private BaiViet baiViet;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;
    
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "binh_luan_cha_id")
    private BinhLuan binhLuanCha;
    
    @OneToMany(mappedBy = "binhLuanCha", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BinhLuan> binhLuanCon = new ArrayList<>();
    
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
