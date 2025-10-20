package org.example.mapping;

import org.example.dto.DanhMucDto.DanhMucCreateDto;
import org.example.dto.DanhMucDto.DanhMucResponseDto;
import org.example.dto.DanhMucDto.DanhMucUpdateDto;
import org.example.entity.DanhMuc;
import org.springframework.stereotype.Component;

@Component
public class DanhMucMapper {

    public DanhMucResponseDto toResponseDTO(DanhMuc entity) {
        if (entity == null) return null;

        DanhMucResponseDto dto = new DanhMucResponseDto();
        dto.setId(entity.getId());
        dto.setDanhMucCha(entity.getDanhMucCha());
        dto.setTen(entity.getTen());
        dto.setMoTa(entity.getMoTa());
        dto.setDuongDan(entity.getDuongDan());
        dto.setThuTu(entity.getThuTu());
        dto.setKichHoat(entity.getKichHoat());
        dto.setNgayTao(entity.getNgayTao());
        dto.setNgayCapNhat(entity.getNgayCapNhat());

        return dto;
    }

    public DanhMuc toEntity(DanhMucCreateDto dto) {
        if (dto == null) return null;

        DanhMuc entity = new DanhMuc();
        entity.setDanhMucCha(dto.getDanhMucCha());
        entity.setTen(dto.getTen());
        entity.setMoTa(dto.getMoTa());
        entity.setDuongDan(dto.getDuongDan());
        entity.setThuTu(dto.getThuTu());
        entity.setKichHoat(dto.getKichHoat());

        return entity;
    }

    public void updateEntityFromDto(DanhMuc entity, DanhMucUpdateDto dto) {
        if (entity == null || dto == null) return;

        if (dto.getDanhMucCha() != null) {
            entity.setDanhMucCha(dto.getDanhMucCha());
        }
        if (dto.getTen() != null) {
            entity.setTen(dto.getTen());
        }
        if (dto.getMoTa() != null) {
            entity.setMoTa(dto.getMoTa());
        }
        if (dto.getDuongDan() != null) {
            entity.setDuongDan(dto.getDuongDan());
        }
        if (dto.getThuTu() != null) {
            entity.setThuTu(dto.getThuTu());
        }
        if (dto.getKichHoat() != null) {
            entity.setKichHoat(dto.getKichHoat());
        }
    }
}