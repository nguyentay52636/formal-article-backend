package org.example.service;

import org.example.dto.BaiVietDto.BaiVietCreateDto;
import org.example.dto.BaiVietDto.BaiVietResponseDto;
import org.example.dto.BaiVietDto.BaiVietUpdateDto;
import org.example.entity.*;
import org.example.mapping.BaiVietMapper;
import org.example.mapping.BaiVietSimpleMapper;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaiVietService {

    @Autowired
    private BaiVietRepository baiVietRepository;

    @Autowired
    private BaiVietMapper baiVietMapper;

    @Autowired
    private BaiVietSimpleMapper baiVietSimpleMapper;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private TheRepository theRepository;

    @Autowired
    private TepTinRepository tepTinRepository;

    // Create
    public BaiVietResponseDto createBaiViet(BaiVietCreateDto createDto, Long tacGiaId) {
        // Validate danh mục
        if (!danhMucRepository.existsById(createDto.getDanhMucId())) {
            throw new RuntimeException("Danh mục không tồn tại với ID: " + createDto.getDanhMucId());
        }

        // Validate tác giả
        NguoiDung tacGia = nguoiDungRepository.findById(tacGiaId)
                .orElseThrow(() -> new RuntimeException("Tác giả không tồn tại với ID: " + tacGiaId));

        // Check đường dẫn đã tồn tại
        if (baiVietRepository.existsByDuongDanAndIdNot(createDto.getDuongDan(), 0L)) {
            throw new RuntimeException("Đường dẫn đã tồn tại: " + createDto.getDuongDan());
        }

        // Convert DTO to Entity
        BaiViet baiViet = baiVietMapper.toEntity(createDto);
        
        // Set tác giả
        baiViet.setTacGia(tacGia);

        // Set ngày xuất bản nếu trạng thái là XUAT_BAN
        if (createDto.getTrangThai() == BaiViet.TrangThai.XUAT_BAN && createDto.getNgayXuatBan() == null) {
            baiViet.setNgayXuatBan(LocalDateTime.now());
        }

        // Save bài viết trước để có ID
        BaiViet savedBaiViet = baiVietRepository.save(baiViet);

        // Xử lý thẻ
        if (createDto.getTheIds() != null && !createDto.getTheIds().isEmpty()) {
            Set<The> thes = theRepository.findAllById(createDto.getTheIds()).stream()
                    .collect(Collectors.toSet());
            savedBaiViet.setThes(thes);
        }

        // Xử lý tệp tin đính kèm
        if (createDto.getTepTinIds() != null && !createDto.getTepTinIds().isEmpty()) {
            List<TepTin> tepTins = tepTinRepository.findAllById(createDto.getTepTinIds());
            // Tạo BaiVietTepTin entities
            for (int i = 0; i < tepTins.size(); i++) {
                BaiVietTepTin baiVietTepTin = new BaiVietTepTin();
                baiVietTepTin.setBaiViet(savedBaiViet);
                baiVietTepTin.setTepTin(tepTins.get(i));
                baiVietTepTin.setLoaiLienKet(BaiVietTepTin.LoaiLienKet.TEP_DINH_KEM);
                baiVietTepTin.setThuTu(i);
                savedBaiViet.getTepTins().add(baiVietTepTin);
            }
        }

        // Save lại với relationships
        savedBaiViet = baiVietRepository.save(savedBaiViet);

        return baiVietMapper.toResponseDto(savedBaiViet);
    }

    // Read All without pagination
    public List<BaiVietResponseDto> getAllBaiViet() {
        return baiVietRepository.findAll()
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Read All with pagination
    public Page<BaiVietResponseDto> getAllBaiViet(Pageable pageable) {
        return baiVietRepository.findAll(pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Read by ID
    public BaiVietResponseDto getBaiVietById(Long id) {
        BaiViet baiViet = baiVietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại với ID: " + id));
        return baiVietMapper.toResponseDto(baiViet);
    }

    // Read by đường dẫn
    public BaiVietResponseDto getBaiVietByDuongDan(String duongDan) {
        BaiViet baiViet = baiVietRepository.findByDuongDan(duongDan)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại với đường dẫn: " + duongDan));
        return baiVietMapper.toResponseDto(baiViet);
    }

    // Get published articles without pagination
    public List<BaiVietResponseDto> getPublishedArticles() {
        return baiVietRepository.findPublishedArticles(LocalDateTime.now(), BaiViet.TrangThai.XUAT_BAN)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get published articles with pagination
    public Page<BaiVietResponseDto> getPublishedArticles(Pageable pageable) {
        return baiVietRepository.findPublishedArticles(LocalDateTime.now(), BaiViet.TrangThai.XUAT_BAN, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Search by keyword without pagination
    public List<BaiVietResponseDto> searchByKeyword(String keyword) {
        return baiVietRepository.searchByKeyword(keyword, BaiViet.TrangThai.XUAT_BAN)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Search by keyword with pagination
    public Page<BaiVietResponseDto> searchByKeyword(String keyword, Pageable pageable) {
        return baiVietRepository.searchByKeyword(keyword, BaiViet.TrangThai.XUAT_BAN, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Get by danh mục without pagination
    public List<BaiVietResponseDto> getBaiVietByDanhMuc(Long danhMucId) {
        return baiVietRepository.findByDanhMucId(danhMucId)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get by danh mục without pagination (simple version without BaiVietTepTin)
    public List<BaiVietResponseDto> getBaiVietByDanhMucSimple(Long danhMucId) {
        return baiVietRepository.findByDanhMucId(danhMucId)
                .stream()
                .map(baiVietSimpleMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    // Get by danh mục with pagination
    public Page<BaiVietResponseDto> getBaiVietByDanhMuc(Long danhMucId, Pageable pageable) {
        return baiVietRepository.findByDanhMucId(danhMucId, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Get by tác giả without pagination
    public List<BaiVietResponseDto> getBaiVietByTacGia(Long tacGiaId) {
        return baiVietRepository.findByTacGiaId(tacGiaId)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get by tác giả with pagination
    public Page<BaiVietResponseDto> getBaiVietByTacGia(Long tacGiaId, Pageable pageable) {
        return baiVietRepository.findByTacGiaId(tacGiaId, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Get by thẻ without pagination
    public List<BaiVietResponseDto> getBaiVietByThe(Long theId) {
        return baiVietRepository.findByTheId(theId, BaiViet.TrangThai.XUAT_BAN)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get by thẻ with pagination
    public Page<BaiVietResponseDto> getBaiVietByThe(Long theId, Pageable pageable) {
        return baiVietRepository.findByTheId(theId, BaiViet.TrangThai.XUAT_BAN, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Get latest articles without pagination
    public List<BaiVietResponseDto> getLatestArticles() {
        return baiVietRepository.findLatestArticles(BaiViet.TrangThai.XUAT_BAN)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get latest articles with pagination
    public Page<BaiVietResponseDto> getLatestArticles(Pageable pageable) {
        return baiVietRepository.findLatestArticles(BaiViet.TrangThai.XUAT_BAN, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Get popular articles without pagination
    public List<BaiVietResponseDto> getPopularArticles() {
        return baiVietRepository.findPopularArticles(BaiViet.TrangThai.XUAT_BAN)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get popular articles with pagination
    public Page<BaiVietResponseDto> getPopularArticles(Pageable pageable) {
        return baiVietRepository.findPopularArticles(BaiViet.TrangThai.XUAT_BAN, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Get by date range without pagination
    public List<BaiVietResponseDto> getBaiVietByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return baiVietRepository.findByDateRange(startDate, endDate, BaiViet.TrangThai.XUAT_BAN)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get by date range with pagination
    public Page<BaiVietResponseDto> getBaiVietByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return baiVietRepository.findByDateRange(startDate, endDate, BaiViet.TrangThai.XUAT_BAN, pageable)
                .map(baiVietMapper::toResponseDto);
    }

    // Update
    public BaiVietResponseDto updateBaiViet(Long id, BaiVietUpdateDto updateDto) {
        BaiViet baiViet = baiVietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại với ID: " + id));

        // Check đường dẫn nếu có thay đổi
        if (updateDto.getDuongDan() != null && !updateDto.getDuongDan().equals(baiViet.getDuongDan())) {
            if (baiVietRepository.existsByDuongDanAndIdNot(updateDto.getDuongDan(), id)) {
                throw new RuntimeException("Đường dẫn đã tồn tại: " + updateDto.getDuongDan());
            }
        }

        // Validate danh mục nếu có thay đổi
        if (updateDto.getDanhMucId() != null && !updateDto.getDanhMucId().equals(baiViet.getDanhMuc().getId())) {
            if (!danhMucRepository.existsById(updateDto.getDanhMucId())) {
                throw new RuntimeException("Danh mục không tồn tại với ID: " + updateDto.getDanhMucId());
            }
        }

        // Update entity
        baiVietMapper.updateEntity(updateDto, baiViet);

        // Set ngày xuất bản nếu trạng thái thay đổi thành XUAT_BAN
        if (updateDto.getTrangThai() == BaiViet.TrangThai.XUAT_BAN && baiViet.getNgayXuatBan() == null) {
            baiViet.setNgayXuatBan(LocalDateTime.now());
        }

        // Update thẻ
        if (updateDto.getTheIds() != null) {
            Set<The> thes = theRepository.findAllById(updateDto.getTheIds()).stream()
                    .collect(Collectors.toSet());
            baiViet.setThes(thes);
        }

        // Update tệp tin đính kèm
        if (updateDto.getTepTinIds() != null) {
            // Xóa tệp tin cũ
            baiViet.getTepTins().clear();
            
            // Thêm tệp tin mới
            List<TepTin> tepTins = tepTinRepository.findAllById(updateDto.getTepTinIds());
            for (int i = 0; i < tepTins.size(); i++) {
                BaiVietTepTin baiVietTepTin = new BaiVietTepTin();
                baiVietTepTin.setBaiViet(baiViet);
                baiVietTepTin.setTepTin(tepTins.get(i));
                baiVietTepTin.setLoaiLienKet(BaiVietTepTin.LoaiLienKet.TEP_DINH_KEM);
                baiVietTepTin.setThuTu(i);
                baiViet.getTepTins().add(baiVietTepTin);
            }
        }

        BaiViet updatedBaiViet = baiVietRepository.save(baiViet);
        return baiVietMapper.toResponseDto(updatedBaiViet);
    }

    // Delete
    public void deleteBaiViet(Long id) {
        if (!baiVietRepository.existsById(id)) {
            throw new RuntimeException("Bài viết không tồn tại với ID: " + id);
        }
        baiVietRepository.deleteById(id);
    }

    // Publish article
    public BaiVietResponseDto publishBaiViet(Long id) {
        BaiViet baiViet = baiVietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại với ID: " + id));

        baiViet.setTrangThai(BaiViet.TrangThai.XUAT_BAN);
        if (baiViet.getNgayXuatBan() == null) {
            baiViet.setNgayXuatBan(LocalDateTime.now());
        }

        BaiViet publishedBaiViet = baiVietRepository.save(baiViet);
        return baiVietMapper.toResponseDto(publishedBaiViet);
    }

    // Unpublish article
    public BaiVietResponseDto unpublishBaiViet(Long id) {
        BaiViet baiViet = baiVietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại với ID: " + id));

        baiViet.setTrangThai(BaiViet.TrangThai.NHAP);

        BaiViet unpublishedBaiViet = baiVietRepository.save(baiViet);
        return baiVietMapper.toResponseDto(unpublishedBaiViet);
    }

    // Get featured articles (noi bat bai viet)
    public List<BaiVietResponseDto> getNoiBatBaiViet() {
        return baiVietRepository.findNoiBatBaiViet(BaiViet.TrangThai.XUAT_BAN)
                .stream()
                .map(baiVietMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Change article status
    public BaiVietResponseDto thayDoiTrangThai(Long id, BaiViet.TrangThai trangThai) {
        BaiViet baiViet = baiVietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại với ID: " + id));

        baiViet.setTrangThai(trangThai);
        
        // Nếu chuyển sang XUAT_BAN và chưa có ngày xuất bản
        if (trangThai == BaiViet.TrangThai.XUAT_BAN && baiViet.getNgayXuatBan() == null) {
            baiViet.setNgayXuatBan(LocalDateTime.now());
        }

        BaiViet updatedBaiViet = baiVietRepository.save(baiViet);
        return baiVietMapper.toResponseDto(updatedBaiViet);
    }

    // Get statistics
    public long countByTrangThai(BaiViet.TrangThai trangThai) {
        return baiVietRepository.countByTrangThai(trangThai);
    }

    public long countByTacGia(Long tacGiaId) {
        return baiVietRepository.countByTacGiaId(tacGiaId);
    }

    public long countByDanhMuc(Long danhMucId) {
        return baiVietRepository.countByDanhMucId(danhMucId);
    }

    public java.util.Map<String, Object> getArticleStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("publishedCount", countByTrangThai(BaiViet.TrangThai.XUAT_BAN));
        stats.put("draftCount", countByTrangThai(BaiViet.TrangThai.NHAP));
        stats.put("archivedCount", countByTrangThai(BaiViet.TrangThai.LUU_TRU));
        return stats;
    }
}
