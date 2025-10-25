package org.example.dto.TepTin;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TepTinResponseDto {
    private Long id;
    private String ten;
    private String duongDan;
    private String loaiTep;
    private String tenTapTin;
    private String dinhDang;
    private Long kichThuoc;
    private String duongDanLuu;
    private Integer chieuRong;
    private Integer chieuCao;
    private String moTa;
    private Long nguoiTaoId;
    private String nguoiTaoTen;
    private String trangThai;
    private String thongTinBoSung;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayTao;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayCapNhat;
    
    // Statistics fields
    private Long luotTai;
    private Long luotXem;
}
