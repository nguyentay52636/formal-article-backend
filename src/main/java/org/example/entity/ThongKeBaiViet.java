package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "thong_ke_bai_viet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeBaiViet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "bai_viet_id", nullable = false)
    private Long baiVietId;
    
    @Column(name = "so_luot_xem")
    private Long soLuotXem = 0L;
    
    @Column(name = "so_luot_thich")
    private Long soLuotThich = 0L;
    
    @Column(name = "so_luot_chia_se")
    private Long soLuotChiaSe = 0L;
    
    @Column(name = "so_luong_binh_luan")
    private Long soLuongBinhLuan = 0L;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
