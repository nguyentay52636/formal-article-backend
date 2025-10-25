package org.example.service;

import org.example.dto.DanhMucDto.DanhMucCreateDto;
import org.example.dto.DanhMucDto.DanhMucResponseDto;
import org.example.dto.DanhMucDto.DanhMucUpdateDto;
import org.example.dto.BaiVietDto.BaiVietResponseDto;
import org.example.entity.DanhMuc;
import org.example.mapping.DanhMucMapper;
import org.example.repository.DanhMucRepository;
import org.example.service.BaiVietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private DanhMucMapper danhMucMapper;

    @Autowired
    private BaiVietService baiVietService;

    // Create
    public DanhMucResponseDto createDanhMuc(DanhMucCreateDto requestDTO) {
        // Check if ten already exists
        if (danhMucRepository.existsByTen(requestDTO.getTen())) {
            throw new RuntimeException("Tên danh mục đã tồn tại: " + requestDTO.getTen());
        }

        // Check if duong dan already exists
        if (requestDTO.getDuongDan() != null && 
            danhMucRepository.existsByDuongDan(requestDTO.getDuongDan())) {
            throw new RuntimeException("Đường dẫn đã tồn tại: " + requestDTO.getDuongDan());
        }

        DanhMuc danhMuc = danhMucMapper.toEntity(requestDTO);
        DanhMuc savedDanhMuc = danhMucRepository.save(danhMuc);
        return danhMucMapper.toResponseDTO(savedDanhMuc);
    }

    // Read All - Sử dụng mapper
    public List<DanhMucResponseDto> getAllDanhMuc() {
        return danhMucRepository.findAllOrderByThuTu().stream()
                .map(danhMucMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Read All Active - Sử dụng mapper
    public List<DanhMucResponseDto> getAllActiveDanhMuc() {
        return danhMucRepository.findActiveOrderByThuTu().stream()
                .map(danhMucMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Read by ID - Sử dụng mapper
    public DanhMucResponseDto getDanhMucById(Long id) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));
        return danhMucMapper.toResponseDTO(danhMuc);
    }

    // Read by duong dan - Sử dụng mapper
    public DanhMucResponseDto getDanhMucByDuongDan(String duongDan) {
        DanhMuc danhMuc = danhMucRepository.findByDuongDan(duongDan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với đường dẫn: " + duongDan));
        return danhMucMapper.toResponseDTO(danhMuc);
    }

    // Read by ten - Sử dụng mapper
    public DanhMucResponseDto getDanhMucByTen(String ten) {
        DanhMuc danhMuc = danhMucRepository.findByTen(ten)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với tên: " + ten));
        return danhMucMapper.toResponseDTO(danhMuc);
    }

    // Read by kich hoat status - Sử dụng mapper
    public List<DanhMucResponseDto> getDanhMucByKichHoat(Boolean kichHoat) {
        return danhMucRepository.findByKichHoat(kichHoat).stream()
                .map(danhMucMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Search by keyword - Sử dụng mapper
    public List<DanhMucResponseDto> searchDanhMuc(String keyword) {
        return danhMucRepository.searchByKeyword(keyword).stream()
                .map(danhMucMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update - Sử dụng mapper
    public DanhMucResponseDto updateDanhMuc(Long id, DanhMucUpdateDto updateDTO) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));

        // Update ten if provided and not already in use
        if (updateDTO.getTen() != null && !updateDTO.getTen().equals(danhMuc.getTen())) {
            if (danhMucRepository.existsByTenAndIdNot(updateDTO.getTen(), id)) {
                throw new RuntimeException("Tên danh mục đã tồn tại: " + updateDTO.getTen());
            }
        }

        // Update duongDan if provided and not already in use
        if (updateDTO.getDuongDan() != null && !updateDTO.getDuongDan().equals(danhMuc.getDuongDan())) {
            if (danhMucRepository.existsByDuongDanAndIdNot(updateDTO.getDuongDan(), id)) {
                throw new RuntimeException("Đường dẫn đã tồn tại: " + updateDTO.getDuongDan());
            }
        }

        // Update entity using mapper
        danhMucMapper.updateEntityFromDto(danhMuc, updateDTO);

        DanhMuc updatedDanhMuc = danhMucRepository.save(danhMuc);
        return danhMucMapper.toResponseDTO(updatedDanhMuc);
    }

    // Delete
    public void deleteDanhMuc(Long id) {
        if (!danhMucRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy danh mục với id: " + id);
        }
        danhMucRepository.deleteById(id);
    }

    // Toggle kich hoat status
    public DanhMucResponseDto toggleKichHoat(Long id) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));
        
        danhMuc.setKichHoat(!danhMuc.getKichHoat());
        DanhMuc updatedDanhMuc = danhMucRepository.save(danhMuc);
        return danhMucMapper.toResponseDTO(updatedDanhMuc);
    }

    // Update thu tu
    public DanhMucResponseDto updateThuTu(Long id, Integer thuTu) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));
        
        danhMuc.setThuTu(thuTu);
        DanhMuc updatedDanhMuc = danhMucRepository.save(danhMuc);
        return danhMucMapper.toResponseDTO(updatedDanhMuc);
    }

    // Get articles by category
    public List<BaiVietResponseDto> getBaiVietByDanhMuc(Long danhMucId) {
        // Check if category exists
        if (!danhMucRepository.existsById(danhMucId)) {
            throw new RuntimeException("Không tìm thấy danh mục với id: " + danhMucId);
        }
        
        return baiVietService.getBaiVietByDanhMuc(danhMucId);
    }
}
