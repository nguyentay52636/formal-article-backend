package org.example.mapping;

import org.example.dto.HoatDongDto.HoatDongCreateDto;
import org.example.dto.HoatDongDto.HoatDongResponseDto;
import org.example.dto.HoatDongDto.HoatDongUpdateDto;
import org.example.entity.LichSuHoatDong;
import org.example.entity.NguoiDung;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HoatDongMapper {

    public LichSuHoatDong toEntity(HoatDongCreateDto dto) {
        if (dto == null) return null;

        LichSuHoatDong entity = new LichSuHoatDong();
        entity.setNguoiThucHienId(dto.getNguoiThucHienId());
        entity.setDoiTuong(dto.getDoiTuong());
        entity.setDoiTuongId(dto.getDoiTuongId());
        entity.setHanhDong(dto.getHanhDong());
        entity.setTruoc(dto.getTruoc());
        entity.setSau(dto.getSau());
        entity.setThongTinBoSung(dto.getThongTinBoSung());
        
        return entity;
    }

    public void updateEntity(HoatDongUpdateDto updateDto, LichSuHoatDong entity) {
        if (updateDto == null || entity == null) return;

        if (updateDto.getDoiTuong() != null) {
            entity.setDoiTuong(updateDto.getDoiTuong());
        }
        if (updateDto.getDoiTuongId() != null) {
            entity.setDoiTuongId(updateDto.getDoiTuongId());
        }
        if (updateDto.getHanhDong() != null) {
            entity.setHanhDong(updateDto.getHanhDong());
        }
        if (updateDto.getTruoc() != null) {
            entity.setTruoc(updateDto.getTruoc());
        }
        if (updateDto.getSau() != null) {
            entity.setSau(updateDto.getSau());
        }
        if (updateDto.getThongTinBoSung() != null) {
            entity.setThongTinBoSung(updateDto.getThongTinBoSung());
        }
    }

    public HoatDongResponseDto toResponseDto(LichSuHoatDong entity) {
        if (entity == null) return null;

        HoatDongResponseDto dto = new HoatDongResponseDto();
        dto.setId(entity.getId());
        dto.setNguoiThucHienId(entity.getNguoiThucHienId());
        dto.setDoiTuong(entity.getDoiTuong());
        dto.setDoiTuongId(entity.getDoiTuongId());
        dto.setHanhDong(entity.getHanhDong());
        dto.setTruoc(entity.getTruoc());
        dto.setSau(entity.getSau());
        dto.setNgayTao(entity.getNgayTao());
        dto.setThongTinBoSung(entity.getThongTinBoSung());

        return dto;
    }

    public HoatDongResponseDto toResponseDtoWithUser(LichSuHoatDong entity, NguoiDung nguoiDung) {
        HoatDongResponseDto dto = toResponseDto(entity);
        if (dto != null && nguoiDung != null) {
            dto.setNguoiThucHien(mapUserToDto(nguoiDung));
        }
        return dto;
    }

    public List<HoatDongResponseDto> toResponseDtoList(List<LichSuHoatDong> entities) {
        if (entities == null) return null;
        
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private HoatDongResponseDto.UserInfoDto mapUserToDto(NguoiDung nguoiDung) {
        if (nguoiDung == null) return null;

        HoatDongResponseDto.UserInfoDto userDto = new HoatDongResponseDto.UserInfoDto();
        userDto.setId(nguoiDung.getId());
        userDto.setTenDangNhap(nguoiDung.getTenDangNhap());
        userDto.setHoTen(nguoiDung.getHoTen());
        userDto.setAnhDaiDienId(nguoiDung.getAnhDaiDien() != null ? nguoiDung.getAnhDaiDien().getId() : null);

        return userDto;
    }
}