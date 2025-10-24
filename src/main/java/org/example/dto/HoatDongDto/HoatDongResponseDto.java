package org.example.dto.HoatDongDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoatDongResponseDto {
    
    private Long id;
    
    private Long nguoiThucHienId;
    
    private String doiTuong;
    
    private Long doiTuongId;
    
    private String hanhDong;
    
    private String truoc;
    
    private String sau;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayTao;
    
    private String thongTinBoSung;
    
    // Thông tin người thực hiện
    private UserInfoDto nguoiThucHien;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDto {
        private Long id;
        private String tenDangNhap;
        private String hoTen;
        private Long anhDaiDienId;
    }
}
