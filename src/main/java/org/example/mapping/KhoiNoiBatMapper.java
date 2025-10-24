package org.example.mapping;

import org.example.dto.KhoiNoiBat.KhoiNoiBatCreateDto;
import org.example.dto.KhoiNoiBat.KhoiNoiBatResponseDto;
import org.example.dto.KhoiNoiBat.KhoiNoiBatUpdateDto;
import org.example.entity.KhoiNoiBat;
import org.springframework.stereotype.Component;

@Component
public class KhoiNoiBatMapper {

    public KhoiNoiBatResponseDto toResponseDTO(KhoiNoiBat entity) {
        if (entity == null) return null;

        KhoiNoiBatResponseDto dto = new KhoiNoiBatResponseDto();
        dto.setId(entity.getId());
        dto.setMa(entity.getMa());
        dto.setTen(entity.getTen());
        dto.setCauHinh(entity.getCauHinh());
        dto.setKichHoat(entity.getKichHoat());
        dto.setNgayTao(entity.getNgayTao());
        dto.setNgayCapNhat(entity.getNgayCapNhat());

        return dto;
    }

    public KhoiNoiBat toEntity(KhoiNoiBatCreateDto dto) {
        if (dto == null) return null;

        KhoiNoiBat entity = new KhoiNoiBat();
        entity.setMa(dto.getMa());
        entity.setTen(dto.getTen());
        entity.setCauHinh(dto.getCauHinh());
        entity.setKichHoat(dto.getKichHoat() != null ? dto.getKichHoat() : true);

        return entity;
    }

    public void updateEntityFromDto(KhoiNoiBat entity, KhoiNoiBatUpdateDto dto) {
        if (entity == null || dto == null) return;

        if (dto.getMa() != null) {
            entity.setMa(dto.getMa());
        }
        if (dto.getTen() != null) {
            entity.setTen(dto.getTen());
        }
        if (dto.getCauHinh() != null) {
            entity.setCauHinh(dto.getCauHinh());
        }
        if (dto.getKichHoat() != null) {
            entity.setKichHoat(dto.getKichHoat());
        }
    }
}
