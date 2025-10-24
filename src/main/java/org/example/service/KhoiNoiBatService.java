package org.example.service;

import org.example.dto.KhoiNoiBat.KhoiNoiBatCreateDto;
import org.example.dto.KhoiNoiBat.KhoiNoiBatResponseDto;
import org.example.dto.KhoiNoiBat.KhoiNoiBatUpdateDto;
import org.example.entity.KhoiNoiBat;
import org.example.mapping.KhoiNoiBatMapper;
import org.example.repository.KhoiNoiBatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KhoiNoiBatService {

    @Autowired
    private KhoiNoiBatRepository khoiNoiBatRepository;

    @Autowired
    private KhoiNoiBatMapper khoiNoiBatMapper;

    public KhoiNoiBatResponseDto createKhoiNoiBat(KhoiNoiBatCreateDto requestDTO) {
        if (khoiNoiBatRepository.existsByMa(requestDTO.getMa())) {
            throw new RuntimeException("Mã khối nổi bật đã tồn tại: " + requestDTO.getMa());
        }
        if (khoiNoiBatRepository.existsByTen(requestDTO.getTen())) {
            throw new RuntimeException("Tên khối nổi bật đã tồn tại: " + requestDTO.getTen());
        }

        KhoiNoiBat khoiNoiBat = khoiNoiBatMapper.toEntity(requestDTO);
        KhoiNoiBat savedKhoiNoiBat = khoiNoiBatRepository.save(khoiNoiBat);
        return khoiNoiBatMapper.toResponseDTO(savedKhoiNoiBat);
    }

    // Read All
    public List<KhoiNoiBatResponseDto> getAllKhoiNoiBat() {
        return khoiNoiBatRepository.findAllOrderByNgayTaoDesc().stream()
                .map(khoiNoiBatMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Read All Active
    public List<KhoiNoiBatResponseDto> getAllActiveKhoiNoiBat() {
        return khoiNoiBatRepository.findActiveOrderByNgayTaoDesc().stream()
                .map(khoiNoiBatMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Read by ID
    public KhoiNoiBatResponseDto getKhoiNoiBatById(Long id) {
        KhoiNoiBat khoiNoiBat = khoiNoiBatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khối nổi bật với id: " + id));
        return khoiNoiBatMapper.toResponseDTO(khoiNoiBat);
    }

    // Read by ma
    public KhoiNoiBatResponseDto getKhoiNoiBatByMa(String ma) {
        KhoiNoiBat khoiNoiBat = khoiNoiBatRepository.findByMa(ma)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khối nổi bật với mã: " + ma));
        return khoiNoiBatMapper.toResponseDTO(khoiNoiBat);
    }

    // Read by ten
    public KhoiNoiBatResponseDto getKhoiNoiBatByTen(String ten) {
        KhoiNoiBat khoiNoiBat = khoiNoiBatRepository.findByTen(ten)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khối nổi bật với tên: " + ten));
        return khoiNoiBatMapper.toResponseDTO(khoiNoiBat);
    }

    // Read by kich hoat status
    public List<KhoiNoiBatResponseDto> getKhoiNoiBatByKichHoat(Boolean kichHoat) {
        return khoiNoiBatRepository.findByKichHoat(kichHoat).stream()
                .map(khoiNoiBatMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Search by keyword
    public List<KhoiNoiBatResponseDto> searchKhoiNoiBat(String keyword) {
        return khoiNoiBatRepository.searchByKeyword(keyword).stream()
                .map(khoiNoiBatMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update
    public KhoiNoiBatResponseDto updateKhoiNoiBat(Long id, KhoiNoiBatUpdateDto updateDTO) {
        KhoiNoiBat khoiNoiBat = khoiNoiBatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khối nổi bật với id: " + id));

        if (updateDTO.getMa() != null && !updateDTO.getMa().equals(khoiNoiBat.getMa())) {
            if (khoiNoiBatRepository.existsByMaAndIdNot(updateDTO.getMa(), id)) {
                throw new RuntimeException("Mã khối nổi bật đã tồn tại: " + updateDTO.getMa());
            }
        }

        // Update ten if provided and not already in use
        if (updateDTO.getTen() != null && !updateDTO.getTen().equals(khoiNoiBat.getTen())) {
            if (khoiNoiBatRepository.existsByTenAndIdNot(updateDTO.getTen(), id)) {
                throw new RuntimeException("Tên khối nổi bật đã tồn tại: " + updateDTO.getTen());
            }
        }

        // Update entity using mapper
        khoiNoiBatMapper.updateEntityFromDto(khoiNoiBat, updateDTO);

        KhoiNoiBat updatedKhoiNoiBat = khoiNoiBatRepository.save(khoiNoiBat);
        return khoiNoiBatMapper.toResponseDTO(updatedKhoiNoiBat);
    }

    // Delete
    public void deleteKhoiNoiBat(Long id) {
        if (!khoiNoiBatRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy khối nổi bật với id: " + id);
        }
        khoiNoiBatRepository.deleteById(id);
    }

    // Toggle kich hoat status
    public KhoiNoiBatResponseDto toggleKichHoat(Long id) {
        KhoiNoiBat khoiNoiBat = khoiNoiBatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khối nổi bật với id: " + id));
        
        khoiNoiBat.setKichHoat(!khoiNoiBat.getKichHoat());
        KhoiNoiBat updatedKhoiNoiBat = khoiNoiBatRepository.save(khoiNoiBat);
        return khoiNoiBatMapper.toResponseDTO(updatedKhoiNoiBat);
    }
}
