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
@Table(name = "danh_muc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"baiViets"})
@ToString(exclude = {"baiViets"})
public class DanhMuc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "danh_muc_cha")
    private Long danhMucCha;

    @Column(name = "duong_dan", length = 200)
    private String duongDan;
       @Column(name = "ten", nullable = false, length = 100)
    private String ten;
    @Column(name = "mo_ta", length = 500)
    private String moTa;
    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;
    @Column(name = "kich_hoat", nullable = false)
    private Boolean kichHoat;
    
    @CreationTimestamp
    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;
    
    @UpdateTimestamp
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;
    
    // Relationships
    @OneToMany(mappedBy = "danhMuc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BaiViet> baiViets = new ArrayList<>();
}
