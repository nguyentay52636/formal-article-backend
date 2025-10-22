package org.example.dto.HoatDongDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoatDongCreateDto {
    
    @NotNull(message = "Người thực hiện ID không được để trống")
    private Long nguoiThucHienId;
    
    @NotBlank(message = "Đối tượng không được để trống")
    private String doiTuong;
    
    private Long doiTuongId;
    
    @NotBlank(message = "Hành động không được để trống")
    private String hanhDong;
    
    private String truoc;
    
    private String sau;
    
    private String thongTinBoSung;
}
