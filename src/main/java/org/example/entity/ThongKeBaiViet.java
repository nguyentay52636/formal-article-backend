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

@Entity
@Table(name = "thong_ke_bai_viet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"baiViet"})
@ToString(exclude = {"baiViet"})
public class ThongKeBaiViet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bai_viet_id", nullable = false, unique = true)
    private BaiViet baiViet;
    
    @Column(name = "luot_xem", nullable = false)
    private Long luotXem = 0L;
    
    @Column(name = "luot_tai", nullable = false)
    private Long luotTai = 0L;
    
    @Column(name = "so_binh_luan", nullable = false)
    private Long soBinhLuan = 0L;
    
    @Column(name = "lan_xem_cuoi")
    private LocalDateTime lanXemCuoi;
    
    @Column(name = "lan_tai_cuoi")
    private LocalDateTime lanTaiCuoi;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
