package org.example.dto.KhoiNoiBat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhoiNoiBatResponseDto {

    private Long id;
    private String ma;
    private String ten;
    private String cauHinh;
    private Boolean kichHoat;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    
} 