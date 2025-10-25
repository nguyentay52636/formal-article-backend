package org.example.dto.TepTin;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TepTinUpdateDto {
    @Size(max = 255, message = "Tên file không được vượt quá 255 ký tự")
    private String ten;
    
    @Size(max = 500, message = "Đường dẫn không được vượt quá 500 ký tự")
    private String duongDan;
    
    private String loaiTep;
    
    private String tenTapTin;
    
    @Size(max = 100, message = "Định dạng không được vượt quá 100 ký tự")
    private String dinhDang;
    
    private Long kichThuoc;
    
    @Size(max = 500, message = "Đường dẫn lưu không được vượt quá 500 ký tự")
    private String duongDanLuu;
    
    private Integer chieuRong;
    private Integer chieuCao;
    
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String moTa;
    
    private String trangThai;
    private String thongTinBoSung;
}
