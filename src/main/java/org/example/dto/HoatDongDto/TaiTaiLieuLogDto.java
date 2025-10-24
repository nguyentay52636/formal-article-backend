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
public class TaiTaiLieuLogDto {
    
    @NotNull(message = "Người thực hiện ID không được để trống")
    private Long nguoiThucHienId;
    
    @NotNull(message = "Tài liệu ID không được để trống")
    private Long taiLieuId;
    
    @NotBlank(message = "Tên tài liệu không được để trống")
    private String tenTaiLieu;
    
    private String thongTinBoSung;
}
