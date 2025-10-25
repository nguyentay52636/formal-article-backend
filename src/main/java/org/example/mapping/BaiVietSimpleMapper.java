package org.example.mapping;

import org.example.dto.BaiVietDto.BaiVietResponseDto;
import org.example.entity.BaiViet;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BaiVietSimpleMapper {

    public BaiVietResponseDto toSimpleResponseDto(BaiViet baiViet) {
        BaiVietResponseDto dto = new BaiVietResponseDto();
        dto.setId(baiViet.getId());
        dto.setTieuDe(baiViet.getTieuDe());
        dto.setTomTat(baiViet.getTomTat());
        dto.setDuongDan(baiViet.getDuongDan());
        dto.setTrangThai(baiViet.getTrangThai());
        dto.setNgayXuatBan(baiViet.getNgayXuatBan());
        dto.setNgayTao(baiViet.getNgayTao());
        dto.setNgayCapNhat(baiViet.getNgayCapNhat());
        dto.setThongTinBoSung(baiViet.getThongTinBoSung());
        
        if (baiViet.getDanhMuc() != null) {
            dto.setDanhMucId(baiViet.getDanhMuc().getId());
            dto.setDanhMucTen(baiViet.getDanhMuc().getTen());
        }
        if (baiViet.getTacGia() != null) {
            dto.setTacGiaId(baiViet.getTacGia().getId());
            dto.setTacGiaTen(baiViet.getTacGia().getHoTen());
        }
        
        if (baiViet.getAnhDaiDien() != null) {
            dto.setAnhDaiDienId(baiViet.getAnhDaiDien().getId());
            dto.setAnhDaiDienDuongDan(baiViet.getAnhDaiDien().getDuongDan());
        }
        
        if (baiViet.getThes() != null && !baiViet.getThes().isEmpty()) {
            Set<BaiVietResponseDto.TheDto> theDtos = baiViet.getThes().stream()
                    .map(the -> {
                        BaiVietResponseDto.TheDto theDto = new BaiVietResponseDto.TheDto();
                        theDto.setId(the.getId());
                        theDto.setTen(the.getTen());
                        theDto.setDuongDan(the.getDuongDan());
                        theDto.setMauSac(the.getMauSac());
                        return theDto;
                    })
                    .collect(Collectors.toSet());
            dto.setThes(theDtos);
        }
        
        
        return dto;
    }
}
