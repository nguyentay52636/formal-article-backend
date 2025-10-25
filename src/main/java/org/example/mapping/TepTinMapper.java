package org.example.mapping;

import org.example.dto.TepTin.TepTinCreateDto;
import org.example.dto.TepTin.TepTinResponseDto;
import org.example.dto.TepTin.TepTinUpdateDto;
import org.example.entity.TepTin;
import org.springframework.stereotype.Component;

@Component
public class TepTinMapper {
    
    public TepTinResponseDto toResponseDTO(TepTin entity) {
        if (entity == null) return null;
        
        TepTinResponseDto dto = new TepTinResponseDto();
        dto.setId(entity.getId());
        dto.setTen(entity.getTen());
        dto.setDuongDan(entity.getDuongDan());
        dto.setLoaiTep(entity.getLoaiTep());
        dto.setTenTapTin(entity.getTenTapTin());
        dto.setDinhDang(entity.getDinhDang());
        dto.setKichThuoc(entity.getKichThuoc());
        dto.setDuongDanLuu(entity.getDuongDanLuu());
        dto.setChieuRong(entity.getChieuRong());
        dto.setChieuCao(entity.getChieuCao());
        dto.setMoTa(entity.getMoTa());
        dto.setTrangThai(entity.getTrangThai());
        dto.setThongTinBoSung(entity.getThongTinBoSung());
        dto.setNgayTao(entity.getNgayTao());
        dto.setNgayCapNhat(entity.getNgayCapNhat());
        
        // Map user information
        if (entity.getNguoiTao() != null) {
            dto.setNguoiTaoId(entity.getNguoiTao().getId());
            dto.setNguoiTaoTen(entity.getNguoiTao().getHoTen());
        }
        
        // Set default statistics (can be enhanced later)
        dto.setLuotTai(0L);
        dto.setLuotXem(0L);
        
        return dto;
    }
    
    public TepTin toEntity(TepTinCreateDto dto) {
        if (dto == null) return null;
        
        TepTin entity = new TepTin();
        entity.setTen(dto.getTen());
        entity.setDuongDan(dto.getDuongDan());
        entity.setLoaiTep(dto.getLoaiTep());
        entity.setTenTapTin(dto.getTenTapTin());
        entity.setDinhDang(dto.getDinhDang());
        entity.setKichThuoc(dto.getKichThuoc());
        entity.setDuongDanLuu(dto.getDuongDanLuu());
        entity.setChieuRong(dto.getChieuRong());
        entity.setChieuCao(dto.getChieuCao());
        entity.setMoTa(dto.getMoTa());
        entity.setTrangThai(dto.getTrangThai() != null ? dto.getTrangThai() : "active");
        entity.setThongTinBoSung(dto.getThongTinBoSung());
        
        return entity;
    }
    
    public void updateEntityFromDto(TepTin entity, TepTinUpdateDto dto) {
        if (entity == null || dto == null) return;
        
        if (dto.getTen() != null) {
            entity.setTen(dto.getTen());
        }
        if (dto.getDuongDan() != null) {
            entity.setDuongDan(dto.getDuongDan());
        }
        if (dto.getLoaiTep() != null) {
            entity.setLoaiTep(dto.getLoaiTep());
        }
        if (dto.getTenTapTin() != null) {
            entity.setTenTapTin(dto.getTenTapTin());
        }
        if (dto.getDinhDang() != null) {
            entity.setDinhDang(dto.getDinhDang());
        }
        if (dto.getKichThuoc() != null) {
            entity.setKichThuoc(dto.getKichThuoc());
        }
        if (dto.getDuongDanLuu() != null) {
            entity.setDuongDanLuu(dto.getDuongDanLuu());
        }
        if (dto.getChieuRong() != null) {
            entity.setChieuRong(dto.getChieuRong());
        }
        if (dto.getChieuCao() != null) {
            entity.setChieuCao(dto.getChieuCao());
        }
        if (dto.getMoTa() != null) {
            entity.setMoTa(dto.getMoTa());
        }
        if (dto.getTrangThai() != null) {
            entity.setTrangThai(dto.getTrangThai());
        }
        if (dto.getThongTinBoSung() != null) {
            entity.setThongTinBoSung(dto.getThongTinBoSung());
        }
    }
}
