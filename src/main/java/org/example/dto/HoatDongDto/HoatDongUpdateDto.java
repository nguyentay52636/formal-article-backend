package org.example.dto.HoatDongDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoatDongUpdateDto {
    
    private String doiTuong;
    
    private Long doiTuongId;
    
    private String hanhDong;
    
    private String truoc;
    
    private String sau;
    
    private String thongTinBoSung;
}
