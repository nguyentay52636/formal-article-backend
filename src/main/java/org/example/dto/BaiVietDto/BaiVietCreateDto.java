package org.example.dto.BaiVietDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class BaiVietCreateDto {
    
    @NotNull(message = "Danh mục không được để trống")
    private Long danhMucId;
    
    @NotBlank(message = "Đường dẫn không được để trống")
    @Size(max = 180, message = "Đường dẫn không được vượt quá 180 ký tự")
    private String duongDan;
    
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 220, message = "Tiêu đề không được vượt quá 220 ký tự")
    private String tieuDe;
    
    @Size(max = 300, message = "Tóm tắt không được vượt quá 300 ký tự")
    private String tomTat;
    
    private String noiDungHtml;
    
    private BaiViet.TrangThai trangThai = BaiViet.TrangThai.NHAP;
    
    private LocalDateTime ngayXuatBan;
    
    private Long anhDaiDienId;
    
    private String thongTinBoSung;
    
    private Set<Long> theIds;
    
    private List<Long> tepTinIds;
}
