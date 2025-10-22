package org.example.service;

import org.example.dto.HoatDongDto.HoatDongCreateDto;
import org.example.dto.HoatDongDto.HoatDongResponseDto;
import org.example.dto.HoatDongDto.HoatDongUpdateDto;
import org.example.entity.LichSuHoatDong;
import org.example.entity.NguoiDung;
import org.example.mapping.HoatDongMapper;
import org.example.repository.HoatDongRepository;
import org.example.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HoatDongService {
    
    @Autowired
    private HoatDongRepository hoatDongRepository;
    
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private HoatDongMapper hoatDongMapper;
    
    // Tạo hoạt động mới
    public HoatDongResponseDto createHoatDong(HoatDongCreateDto createDto) {
        LichSuHoatDong entity = hoatDongMapper.toEntity(createDto);
        LichSuHoatDong savedEntity = hoatDongRepository.save(entity);
        return hoatDongMapper.toResponseDto(savedEntity);
    }
    
    // Lấy hoạt động theo ID
    @Transactional(readOnly = true)
    public Optional<HoatDongResponseDto> getHoatDongById(Long id) {
        return hoatDongRepository.findById(id)
                .map(hoatDongMapper::toResponseDto);
    }
    
    // Lấy hoạt động theo ID với thông tin người thực hiện
    @Transactional(readOnly = true)
    public Optional<HoatDongResponseDto> getHoatDongByIdWithUser(Long id) {
        Optional<LichSuHoatDong> entityOpt = hoatDongRepository.findById(id);
        if (entityOpt.isPresent()) {
            LichSuHoatDong entity = entityOpt.get();
            Optional<NguoiDung> userOpt = nguoiDungRepository.findById(entity.getNguoiThucHienId());
            return Optional.of(hoatDongMapper.toResponseDtoWithUser(entity, userOpt.orElse(null)));
        }
        return Optional.empty();
    }
    
    // Cập nhật hoạt động
    public Optional<HoatDongResponseDto> updateHoatDong(Long id, HoatDongUpdateDto updateDto) {
        Optional<LichSuHoatDong> entityOpt = hoatDongRepository.findById(id);
        if (entityOpt.isPresent()) {
            LichSuHoatDong entity = entityOpt.get();
            hoatDongMapper.updateEntity(updateDto, entity);
            LichSuHoatDong savedEntity = hoatDongRepository.save(entity);
            return Optional.of(hoatDongMapper.toResponseDto(savedEntity));
        }
        return Optional.empty();
    }
    
    // Xóa hoạt động
    public boolean deleteHoatDong(Long id) {
        if (hoatDongRepository.existsById(id)) {
            hoatDongRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Lấy tất cả hoạt động với phân trang
    @Transactional(readOnly = true)
    public Page<HoatDongResponseDto> getAllHoatDong(Pageable pageable) {
        Page<LichSuHoatDong> entities = hoatDongRepository.findAll(pageable);
        return entities.map(hoatDongMapper::toResponseDto);
    }
    
    // Lấy hoạt động theo người thực hiện
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> getHoatDongByNguoiThucHien(Long nguoiThucHienId) {
        List<LichSuHoatDong> entities = hoatDongRepository.findByNguoiThucHienIdOrderByNgayTaoDesc(nguoiThucHienId);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Lấy hoạt động theo người thực hiện với phân trang
    @Transactional(readOnly = true)
    public Page<HoatDongResponseDto> getHoatDongByNguoiThucHien(Long nguoiThucHienId, Pageable pageable) {
        Page<LichSuHoatDong> entities = hoatDongRepository.findByNguoiThucHienIdOrderByNgayTaoDesc(nguoiThucHienId, pageable);
        return entities.map(hoatDongMapper::toResponseDto);
    }
    
    // Lấy hoạt động theo đối tượng và đối tượng ID
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> getHoatDongByDoiTuong(String doiTuong, Long doiTuongId) {
        List<LichSuHoatDong> entities = hoatDongRepository.findByDoiTuongAndDoiTuongIdOrderByNgayTaoDesc(doiTuong, doiTuongId);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Lấy hoạt động theo hành động
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> getHoatDongByHanhDong(String hanhDong) {
        List<LichSuHoatDong> entities = hoatDongRepository.findByHanhDongOrderByNgayTaoDesc(hanhDong);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Lấy hoạt động trong khoảng thời gian
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> getHoatDongByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<LichSuHoatDong> entities = hoatDongRepository.findByNgayTaoBetweenOrderByNgayTaoDesc(startDate, endDate);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Lấy hoạt động theo người thực hiện và khoảng thời gian
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> getHoatDongByNguoiThucHienAndDateRange(Long nguoiThucHienId, LocalDateTime startDate, LocalDateTime endDate) {
        List<LichSuHoatDong> entities = hoatDongRepository.findByNguoiThucHienIdAndNgayTaoBetweenOrderByNgayTaoDesc(nguoiThucHienId, startDate, endDate);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Lấy hoạt động theo đối tượng, đối tượng ID và khoảng thời gian
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> getHoatDongByDoiTuongAndDateRange(String doiTuong, Long doiTuongId, LocalDateTime startDate, LocalDateTime endDate) {
        List<LichSuHoatDong> entities = hoatDongRepository.findByDoiTuongAndDoiTuongIdAndNgayTaoBetweenOrderByNgayTaoDesc(doiTuong, doiTuongId, startDate, endDate);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Tìm kiếm hoạt động theo từ khóa
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> searchHoatDongByKeyword(String keyword) {
        List<LichSuHoatDong> entities = hoatDongRepository.findByKeyword(keyword);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Tìm kiếm hoạt động theo từ khóa với phân trang
    @Transactional(readOnly = true)
    public Page<HoatDongResponseDto> searchHoatDongByKeyword(String keyword, Pageable pageable) {
        Page<LichSuHoatDong> entities = hoatDongRepository.findByKeyword(keyword, pageable);
        return entities.map(hoatDongMapper::toResponseDto);
    }
    
    // Đếm số hoạt động của một người dùng
    @Transactional(readOnly = true)
    public long countHoatDongByNguoiThucHien(Long nguoiThucHienId) {
        return hoatDongRepository.countByNguoiThucHienId(nguoiThucHienId);
    }
    
    // Đếm số hoạt động theo đối tượng và đối tượng ID
    @Transactional(readOnly = true)
    public long countHoatDongByDoiTuong(String doiTuong, Long doiTuongId) {
        return hoatDongRepository.countByDoiTuongAndDoiTuongId(doiTuong, doiTuongId);
    }
    
    // Đếm số hoạt động theo hành động
    @Transactional(readOnly = true)
    public long countHoatDongByHanhDong(String hanhDong) {
        return hoatDongRepository.countByHanhDong(hanhDong);
    }
    
    // Lấy hoạt động gần đây nhất của một người dùng
    @Transactional(readOnly = true)
    public List<HoatDongResponseDto> getRecentActivitiesByUser(Long nguoiThucHienId, Pageable pageable) {
        List<LichSuHoatDong> entities = hoatDongRepository.findRecentActivitiesByUser(nguoiThucHienId, pageable);
        return hoatDongMapper.toResponseDtoList(entities);
    }
    
    // Xóa tất cả hoạt động
    public void deleteAllHoatDong() {
        hoatDongRepository.deleteAll();
    }
    
    // Ghi log hoạt động (phương thức tiện ích)
    public void logActivity(Long nguoiThucHienId, String doiTuong, Long doiTuongId, String hanhDong, String truoc, String sau, String thongTinBoSung) {
        HoatDongCreateDto createDto = new HoatDongCreateDto();
        createDto.setNguoiThucHienId(nguoiThucHienId);
        createDto.setDoiTuong(doiTuong);
        createDto.setDoiTuongId(doiTuongId);
        createDto.setHanhDong(hanhDong);
        createDto.setTruoc(truoc);
        createDto.setSau(sau);
        createDto.setThongTinBoSung(thongTinBoSung);
        
        createHoatDong(createDto);
    }
}
