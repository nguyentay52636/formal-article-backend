package org.example.dto.HoatDongDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinhLuanLogDto {
    
    @NotNull(message = "Người thực hiện ID không được để trống")
    private Long nguoiThucHienId;
    
    @NotNull(message = "Bài viết ID không được để trống")
    private Long baiVietId;
    
    @NotNull(message = "Bình luận ID không được để trống")
    private Long binhLuanId;
    
    @NotBlank(message = "Nội dung không được để trống")
    private String noiDung;
    
    private String thongTinBoSung;
}
