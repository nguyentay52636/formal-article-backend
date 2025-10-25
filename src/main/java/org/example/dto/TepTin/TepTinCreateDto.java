package org.example.dto.TepTin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TepTinCreateDto {
    @NotBlank(message = "Tên file không được để trống")
    @Size(max = 255, message = "Tên file không được vượt quá 255 ký tự")
    private String ten;
    
    @NotBlank(message = "Đường dẫn không được để trống")
    @Size(max = 500, message = "Đường dẫn không được vượt quá 500 ký tự")
    private String duongDan;
    
    @NotBlank(message = "Loại file không được để trống")
    private String loaiTep;
    
    @NotBlank(message = "Tên tập tin không được để trống")
    private String tenTapTin;
    
    @NotBlank(message = "Định dạng không được để trống")
    @Size(max = 100, message = "Định dạng không được vượt quá 100 ký tự")
    private String dinhDang;
    
    @NotNull(message = "Kích thước không được để trống")
    private Long kichThuoc;
    
    @NotBlank(message = "Đường dẫn lưu không được để trống")
    @Size(max = 500, message = "Đường dẫn lưu không được vượt quá 500 ký tự")
    private String duongDanLuu;
    
    private Integer chieuRong;
    private Integer chieuCao;
    
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String moTa;
    
    @NotNull(message = "Người tạo không được để trống")
    private Long nguoiTaoId;
    
    private String trangThai;
    private String thongTinBoSung;
}
