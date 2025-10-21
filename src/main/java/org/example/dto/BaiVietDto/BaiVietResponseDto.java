package org.example.dto.BaiVietDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.entity.BaiViet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaiVietResponseDto {
    
    private Long id;
    
    private Long danhMucId;
    private String danhMucTen;
    
    private Long tacGiaId;
    private String tacGiaTen;
    
    private String duongDan;
    private String tieuDe;
    private String tomTat;
    private String noiDungHtml;
    private BaiViet.TrangThai trangThai;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayXuatBan;
    
    private Long anhDaiDienId;
    private String anhDaiDienDuongDan;
    
    private String thongTinBoSung;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayTao;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayCapNhat;
    
    // Thông tin thống kê
    private Long soLuongBinhLuan;
    private Long soLuongPhanUng;
    private Long soLuongLuotXem;
    
    // Danh sách thẻ
    private Set<TheDto> thes;
    
    // Danh sách tệp tin đính kèm
    private List<TepTinDto> tepTins;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TheDto {
        private Long id;
        private String ten;
        private String duongDan;
        private String mauSac;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TepTinDto {
        private Long id;
        private String ten;
        private String duongDan;
        private String dinhDang;
        private Long kichThuoc;
        private String moTa;
    }
}
