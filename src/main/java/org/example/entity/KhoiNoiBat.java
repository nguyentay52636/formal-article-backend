package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "khoi_noi_bat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhoiNoiBat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "ma", nullable = false, length = 50, unique = true)
    private String ma;
    
    @Column(name = "ten", nullable = false, length = 100)
    private String ten;
    
    @Column(name = "cau_hinh", columnDefinition = "LONGTEXT")
    private String cauHinh;
    
    @Column(name = "kich_hoat", nullable = false)
    private Boolean kichHoat = true;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
}
