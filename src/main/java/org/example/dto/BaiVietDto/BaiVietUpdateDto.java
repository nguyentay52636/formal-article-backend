package org.example.dto.BaiVietDto;

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
public class BaiVietUpdateDto {
    
    private Long danhMucId;
    
    @Size(max = 180, message = "Đường dẫn không được vượt quá 180 ký tự")
    private String duongDan;
    
    @Size(max = 220, message = "Tiêu đề không được vượt quá 220 ký tự")
    private String tieuDe;
    
    @Size(max = 300, message = "Tóm tắt không được vượt quá 300 ký tự")
    private String tomTat;
    
    private String noiDungHtml;
    
    private BaiViet.TrangThai trangThai;
    
    private LocalDateTime ngayXuatBan;
    
    private Long anhDaiDienId;
    
    private String thongTinBoSung;
    
    private Set<Long> theIds;
    
    private List<Long> tepTinIds;
}
