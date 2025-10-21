package org.example.mapping;

import org.example.dto.BinhLuanDto.BinhLuanResponseDto;
import org.example.dto.BinhLuanDto.BinhLuanUpdateDto;
import org.example.entity.BinhLuan;
import org.example.entity.NguoiDung;
import org.springframework.stereotype.Component;

@Component
public class BinhLuanMapper {

    public BinhLuanResponseDto toResponseDto(BinhLuan entity) {
        if (entity == null) return null;

        BinhLuanResponseDto dto = new BinhLuanResponseDto();
        dto.setId(entity.getId());
        dto.setBaiVietId(entity.getBaiViet() != null ? entity.getBaiViet().getId() : null);
        dto.setNguoiDungId(entity.getNguoiDung() != null ? entity.getNguoiDung().getId() : null);
        dto.setNoiDung(entity.getNoiDung());
        dto.setTenKhach(entity.getTenKhach());
        dto.setEmailKhach(entity.getEmailKhach());
        dto.setBinhLuanChaId(entity.getBinhLuanCha() != null ? entity.getBinhLuanCha().getId() : null);
        dto.setTrangThai(entity.getTrangThai());
        dto.setThongTinBoSung(entity.getThongTinBoSung());
        dto.setNgayTao(entity.getNgayTao());
        dto.setNgayCapNhat(entity.getNgayCapNhat());

        // Set child comments count
        dto.setSoLuongBinhLuanCon(entity.getBinhLuanCon() != null ? entity.getBinhLuanCon().size() : 0);

        // Set user information
        if (entity.getNguoiDung() != null) {
            dto.setNguoiDung(mapUserToDto(entity.getNguoiDung()));
        }

        return dto;
    }

    public void updateEntity(BinhLuanUpdateDto updateDto, BinhLuan entity) {
        if (updateDto == null || entity == null) return;

        if (updateDto.getNoiDung() != null) {
            entity.setNoiDung(updateDto.getNoiDung());
        }
        if (updateDto.getTenKhach() != null) {
            entity.setTenKhach(updateDto.getTenKhach());
        }
        if (updateDto.getEmailKhach() != null) {
            entity.setEmailKhach(updateDto.getEmailKhach());
        }
        if (updateDto.getTrangThai() != null) {
            entity.setTrangThai(updateDto.getTrangThai());
        }
        if (updateDto.getThongTinBoSung() != null) {
            entity.setThongTinBoSung(updateDto.getThongTinBoSung());
        }
    }

    private BinhLuanResponseDto.UserInfoDto mapUserToDto(NguoiDung nguoiDung) {
        if (nguoiDung == null) return null;

        BinhLuanResponseDto.UserInfoDto userDto = new BinhLuanResponseDto.UserInfoDto();
        userDto.setId(nguoiDung.getId());
        userDto.setTenDangNhap(nguoiDung.getTenDangNhap());
        userDto.setHoTen(nguoiDung.getHoTen());
        userDto.setAnhDaiDienId(nguoiDung.getAnhDaiDien() != null ? nguoiDung.getAnhDaiDien().getId() : null);

        return userDto;
    }
}
