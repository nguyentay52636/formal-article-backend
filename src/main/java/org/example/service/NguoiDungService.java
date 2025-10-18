package org.example.service;

import org.example.dto.NguoiDungRequestDTO;
import org.example.dto.NguoiDungResponseDTO;
import org.example.dto.NguoiDungUpdateDTO;
import org.example.entity.NguoiDung;
import org.example.entity.TepTin;
import org.example.entity.VaiTro;
import org.example.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    // Create
    public NguoiDungResponseDTO createNguoiDung(NguoiDungRequestDTO requestDTO) {
        // Check if username already exists
        if (nguoiDungRepository.existsByTenDangNhap(requestDTO.getTenDangNhap())) {
            throw new RuntimeException("Username already exists: " + requestDTO.getTenDangNhap());
        }

        // Check if email already exists
        if (nguoiDungRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + requestDTO.getEmail());
        }

        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(requestDTO.getTenDangNhap());

        // Encode password if encoder is available
        if (passwordEncoder != null) {
            nguoiDung.setMatKhau(passwordEncoder.encode(requestDTO.getMatKhau()));
        } else {
            nguoiDung.setMatKhau(requestDTO.getMatKhau());
        }

        nguoiDung.setEmail(requestDTO.getEmail());
        nguoiDung.setHoTen(requestDTO.getHoTen());
        nguoiDung.setGioiTinh(requestDTO.getGioiTinh());
        nguoiDung.setNgaySinh(requestDTO.getNgaySinh());
        nguoiDung.setDiaChi(requestDTO.getDiaChi());
        nguoiDung.setSoDienThoai(requestDTO.getSoDienThoai());
        nguoiDung.setTrangThai(requestDTO.getTrangThai() != null ? requestDTO.getTrangThai() : "active");
        nguoiDung.setThongTinBoSung(requestDTO.getThongTinBoSung());

        // Set relationships if IDs are provided
        if (requestDTO.getVaiTroId() != null) {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(requestDTO.getVaiTroId());
            nguoiDung.setVaiTro(vaiTro);
        }

        if (requestDTO.getAnhDaiDienId() != null) {
            TepTin tepTin = new TepTin();
            tepTin.setId(requestDTO.getAnhDaiDienId());
            nguoiDung.setAnhDaiDien(tepTin);
        }

        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
        return convertToResponseDTO(savedNguoiDung);
    }

    // Read All
    public List<NguoiDungResponseDTO> getAllNguoiDung() {
        return nguoiDungRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Read by ID
    public NguoiDungResponseDTO getNguoiDungById(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToResponseDTO(nguoiDung);
    }

    // Read by Username
    public NguoiDungResponseDTO getNguoiDungByUsername(String tenDangNhap) {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + tenDangNhap));
        return convertToResponseDTO(nguoiDung);
    }

    // Read by Email
    public NguoiDungResponseDTO getNguoiDungByEmail(String email) {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return convertToResponseDTO(nguoiDung);
    }

    // Search by keyword
    public List<NguoiDungResponseDTO> searchNguoiDung(String keyword) {
        return nguoiDungRepository.searchByKeyword(keyword).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get by status
    public List<NguoiDungResponseDTO> getNguoiDungByStatus(String trangThai) {
        return nguoiDungRepository.findByTrangThai(trangThai).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Update
    public NguoiDungResponseDTO updateNguoiDung(Long id, NguoiDungUpdateDTO updateDTO) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update password if provided
        if (updateDTO.getMatKhau() != null && !updateDTO.getMatKhau().isEmpty()) {
            if (passwordEncoder != null) {
                nguoiDung.setMatKhau(passwordEncoder.encode(updateDTO.getMatKhau()));
            } else {
                nguoiDung.setMatKhau(updateDTO.getMatKhau());
            }
        }

        // Update email if provided and not already in use
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(nguoiDung.getEmail())) {
            if (nguoiDungRepository.existsByEmail(updateDTO.getEmail())) {
                throw new RuntimeException("Email already exists: " + updateDTO.getEmail());
            }
            nguoiDung.setEmail(updateDTO.getEmail());
        }

        // Update other fields if provided
        if (updateDTO.getHoTen() != null) {
            nguoiDung.setHoTen(updateDTO.getHoTen());
        }
        if (updateDTO.getGioiTinh() != null) {
            nguoiDung.setGioiTinh(updateDTO.getGioiTinh());
        }
        if (updateDTO.getNgaySinh() != null) {
            nguoiDung.setNgaySinh(updateDTO.getNgaySinh());
        }
        if (updateDTO.getDiaChi() != null) {
            nguoiDung.setDiaChi(updateDTO.getDiaChi());
        }
        if (updateDTO.getSoDienThoai() != null) {
            nguoiDung.setSoDienThoai(updateDTO.getSoDienThoai());
        }
        if (updateDTO.getTrangThai() != null) {
            nguoiDung.setTrangThai(updateDTO.getTrangThai());
        }
        if (updateDTO.getThongTinBoSung() != null) {
            nguoiDung.setThongTinBoSung(updateDTO.getThongTinBoSung());
        }

        // Update relationships if IDs are provided
        if (updateDTO.getVaiTroId() != null) {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(updateDTO.getVaiTroId());
            nguoiDung.setVaiTro(vaiTro);
        }

        if (updateDTO.getAnhDaiDienId() != null) {
            TepTin tepTin = new TepTin();
            tepTin.setId(updateDTO.getAnhDaiDienId());
            nguoiDung.setAnhDaiDien(tepTin);
        }

        NguoiDung updatedNguoiDung = nguoiDungRepository.save(nguoiDung);
        return convertToResponseDTO(updatedNguoiDung);
    }

    // Delete
    public void deleteNguoiDung(Long id) {
        if (!nguoiDungRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        nguoiDungRepository.deleteById(id);
    }

    // Convert Entity to DTO
    private NguoiDungResponseDTO convertToResponseDTO(NguoiDung nguoiDung) {
        NguoiDungResponseDTO responseDTO = new NguoiDungResponseDTO();
        responseDTO.setId(nguoiDung.getId());
        responseDTO.setTenDangNhap(nguoiDung.getTenDangNhap());
        responseDTO.setEmail(nguoiDung.getEmail());
        responseDTO.setHoTen(nguoiDung.getHoTen());
        responseDTO.setGioiTinh(nguoiDung.getGioiTinh());
        responseDTO.setNgaySinh(nguoiDung.getNgaySinh());
        responseDTO.setDiaChi(nguoiDung.getDiaChi());
        responseDTO.setSoDienThoai(nguoiDung.getSoDienThoai());
        responseDTO.setTrangThai(nguoiDung.getTrangThai());
        responseDTO.setThongTinBoSung(nguoiDung.getThongTinBoSung());
        responseDTO.setNgayTao(nguoiDung.getNgayTao());
        responseDTO.setNgayCapNhat(nguoiDung.getNgayCapNhat());

        if (nguoiDung.getAnhDaiDien() != null) {
            responseDTO.setAnhDaiDienId(nguoiDung.getAnhDaiDien().getId());
        }

        if (nguoiDung.getVaiTro() != null) {
            responseDTO.setVaiTroId(nguoiDung.getVaiTro().getId());
            responseDTO.setVaiTroTen(nguoiDung.getVaiTro().getTen());
        }

        return responseDTO;
    }
}
