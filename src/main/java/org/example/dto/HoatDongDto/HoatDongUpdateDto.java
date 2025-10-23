package org.example.dto.HoatDongDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoatDongUpdateDto {
    
    private String doiTuong;
    
    private Long doiTuongId;
    
    private String hanhDong;
    
    private String truoc;
    
    private String sau;
    
    private String thongTinBoSung;
}
