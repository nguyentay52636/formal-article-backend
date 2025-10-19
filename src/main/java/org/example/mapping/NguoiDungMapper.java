package org.example.mapping;

import org.example.dto.NguoiDungDto.NguoiDungResponseDTO;
import org.example.dto.VaiTroDto.VaiTroResponseDTO;
import org.example.entity.NguoiDung;
import org.example.entity.VaiTro;
import org.springframework.stereotype.Component;

@Component
public class NguoiDungMapper {

    public NguoiDungResponseDTO toResponseDTO(NguoiDung entity) {
        if (entity == null) return null;

        NguoiDungResponseDTO dto = new NguoiDungResponseDTO();
        dto.setId(entity.getId());
        dto.setTenDangNhap(entity.getTenDangNhap());
        dto.setEmail(entity.getEmail());
        dto.setHoTen(entity.getHoTen());
        dto.setGioiTinh(entity.getGioiTinh());
        dto.setNgaySinh(entity.getNgaySinh());
        dto.setDiaChi(entity.getDiaChi());
        dto.setSoDienThoai(entity.getSoDienThoai());
        dto.setTrangThai(entity.getTrangThai());
        dto.setThongTinBoSung(entity.getThongTinBoSung());
        dto.setNgayTao(entity.getNgayTao());
        dto.setNgayCapNhat(entity.getNgayCapNhat());

        // Set anh dai dien ID
        if (entity.getAnhDaiDien() != null) {
            dto.setAnhDaiDienId(entity.getAnhDaiDien().getId());
        }

        // Set vai tro with full information
        if (entity.getVaiTro() != null) {
            dto.setVaiTro(mapVaiTroToDTO(entity.getVaiTro()));
        }

        return dto;
    }

    private VaiTroResponseDTO mapVaiTroToDTO(VaiTro vaiTro) {
        if (vaiTro == null) return null;
        
        return new VaiTroResponseDTO(
            vaiTro.getId(),
            vaiTro.getMa(),
            vaiTro.getTen()
        );
    }
}
