package org.example.dto.DanhMucDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DanhMuc Response DTO")
public class DanhMucResponseDto {
    
    @Schema(description = "ID danh mục", example = "1")
    private Long id;
    
    @Schema(description = "Danh mục cha", example = "Tin tức")
    private String danhMucCha;
    
    @Schema(description = "Tên danh mục", example = "Tin tức công nghệ")
    private String ten;
    
    @Schema(description = "Mô tả danh mục", example = "Tin tức về công nghệ và khoa học")
    private String moTa;
    
    @Schema(description = "Đường dẫn URL", example = "tin-tuc-cong-nghe")
    private String duongDan;
    
    @Schema(description = "Thứ tự hiển thị", example = "1")
    private Integer thuTu;
    
    @Schema(description = "Trạng thái kích hoạt", example = "true")
    private Boolean kichHoat;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Ngày tạo", example = "2024-01-15T10:30:00")
    private LocalDateTime ngayTao;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Ngày cập nhật", example = "2024-01-20T14:45:00")
    private LocalDateTime ngayCapNhat;
}
