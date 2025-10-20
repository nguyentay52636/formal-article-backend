package org.example.dto.DanhMucDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DanhMuc Create DTO")
public class DanhMucCreateDto {
    
    @Schema(description = "Danh mục cha", example = "Tin tức", maxLength = 100)
    @NotBlank(message = "Danh mục cha không được để trống")
    @Size(max = 100, message = "Danh mục cha không được vượt quá 100 ký tự")
    private String danhMucCha;
    
    @Schema(description = "Tên danh mục", example = "Tin tức công nghệ", maxLength = 100)
    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục không được vượt quá 100 ký tự")
    private String ten;
    
    @Schema(description = "Mô tả danh mục", example = "Tin tức về công nghệ và khoa học", maxLength = 500)
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String moTa;
    
    @Schema(description = "Đường dẫn URL", example = "tin-tuc-cong-nghe", maxLength = 200)
    @Size(max = 200, message = "Đường dẫn không được vượt quá 200 ký tự")
    private String duongDan;
    
    @Schema(description = "Thứ tự hiển thị", example = "1")
    @NotNull(message = "Thứ tự không được để trống")
    private Integer thuTu;
    
    @Schema(description = "Trạng thái kích hoạt", example = "true")
    @NotNull(message = "Trạng thái kích hoạt không được để trống")
    private Boolean kichHoat;
}