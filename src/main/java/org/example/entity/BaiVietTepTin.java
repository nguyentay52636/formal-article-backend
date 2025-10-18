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
@Table(name = "bai_viet_tep_tin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"baiViet", "tepTin"})
@ToString(exclude = {"baiViet", "tepTin"})
public class BaiVietTepTin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bai_viet_id", nullable = false)
    private BaiViet baiViet;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tep_tin_id", nullable = false)
    private TepTin tepTin;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_lien_ket", nullable = false)
    private LoaiLienKet loaiLienKet;
    
    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu = 0;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    public enum LoaiLienKet {
        ANH_DAI_DIEN, TEP_DINH_KEM
    }
}
