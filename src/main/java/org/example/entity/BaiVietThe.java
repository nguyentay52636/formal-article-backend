package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bai_viet_the")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaiVietThe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "bai_viet_id", nullable = false)
    private Long baiVietId;
    
    @Column(name = "the_id", nullable = false)
    private Long theId;
    
    @Column(name = "thong_tin_bo_sung", columnDefinition = "LONGTEXT")
    private String thongTinBoSung;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
