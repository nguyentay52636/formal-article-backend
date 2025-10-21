package org.example.service;

import org.example.dto.BinhLuanDto.BinhLuanCreateDto;
import org.example.dto.BinhLuanDto.BinhLuanResponseDto;
import org.example.dto.BinhLuanDto.BinhLuanUpdateDto;
import org.example.entity.BaiViet;
import org.example.entity.BinhLuan;
import org.example.entity.NguoiDung;
import org.example.mapping.BinhLuanMapper;
import org.example.repository.BaiVietRepository;
import org.example.repository.BinhLuanRepository;
import org.example.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BinhLuanService {

    @Autowired
    private BinhLuanRepository binhLuanRepository;

    @Autowired
    private BinhLuanMapper binhLuanMapper;

    @Autowired
    private BaiVietRepository baiVietRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // Create comment
    public BinhLuanResponseDto createBinhLuan(BinhLuanCreateDto createDto) {
        // Validate article exists
        BaiViet baiViet = baiVietRepository.findById(createDto.getBaiVietId())
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại với ID: " + createDto.getBaiVietId()));

        // Validate user if provided
        NguoiDung nguoiDung = null;
        if (createDto.getNguoiDungId() != null) {
            nguoiDung = nguoiDungRepository.findById(createDto.getNguoiDungId())
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + createDto.getNguoiDungId()));
        }

        // Validate parent comment if provided
        BinhLuan binhLuanCha = null;
        if (createDto.getBinhLuanChaId() != null) {
            binhLuanCha = binhLuanRepository.findById(createDto.getBinhLuanChaId())
                    .orElseThrow(() -> new RuntimeException("Bình luận cha không tồn tại với ID: " + createDto.getBinhLuanChaId()));
        }

        // Create new comment
        BinhLuan binhLuan = new BinhLuan();
        binhLuan.setBaiViet(baiViet);
        binhLuan.setNguoiDung(nguoiDung);
        binhLuan.setNoiDung(createDto.getNoiDung());
        binhLuan.setTenKhach(createDto.getTenKhach());
        binhLuan.setEmailKhach(createDto.getEmailKhach());
        binhLuan.setBinhLuanCha(binhLuanCha);
        binhLuan.setTrangThai(createDto.getTrangThai() != null ? createDto.getTrangThai() : "active");
        binhLuan.setThongTinBoSung(createDto.getThongTinBoSung());

        BinhLuan savedBinhLuan = binhLuanRepository.save(binhLuan);
        return binhLuanMapper.toResponseDto(savedBinhLuan);
    }

    // Get all comments
    public List<BinhLuanResponseDto> getAllBinhLuan() {
        return binhLuanRepository.findAll()
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get comment by ID
    public BinhLuanResponseDto getBinhLuanById(Long id) {
        BinhLuan binhLuan = binhLuanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại với ID: " + id));
        return binhLuanMapper.toResponseDto(binhLuan);
    }

    // Get comments by article ID
    public List<BinhLuanResponseDto> getBinhLuanByBaiVietId(Long baiVietId) {
        return binhLuanRepository.findByBaiVietIdAndTrangThai(baiVietId, "active")
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get parent comments by article ID
    public List<BinhLuanResponseDto> getParentCommentsByBaiVietId(Long baiVietId) {
        return binhLuanRepository.findParentCommentsByBaiVietIdAndTrangThai(baiVietId, "active")
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get child comments by parent comment ID
    public List<BinhLuanResponseDto> getChildCommentsByParentId(Long binhLuanChaId) {
        return binhLuanRepository.findChildCommentsByParentIdAndTrangThai(binhLuanChaId, "active")
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get comments by user ID
    public List<BinhLuanResponseDto> getBinhLuanByNguoiDungId(Long nguoiDungId) {
        return binhLuanRepository.findByNguoiDungId(nguoiDungId)
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Search comments by content
    public List<BinhLuanResponseDto> searchBinhLuanByContent(String keyword) {
        return binhLuanRepository.searchByContent(keyword, "active")
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get recent comments
    public List<BinhLuanResponseDto> getRecentBinhLuan() {
        return binhLuanRepository.findRecentComments("active")
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get comments by status
    public List<BinhLuanResponseDto> getBinhLuanByTrangThai(String trangThai) {
        return binhLuanRepository.findByTrangThai(trangThai)
                .stream()
                .map(binhLuanMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Update comment
    public BinhLuanResponseDto updateBinhLuan(Long id, BinhLuanUpdateDto updateDto) {
        BinhLuan binhLuan = binhLuanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại với ID: " + id));

        binhLuanMapper.updateEntity(updateDto, binhLuan);

        BinhLuan updatedBinhLuan = binhLuanRepository.save(binhLuan);
        return binhLuanMapper.toResponseDto(updatedBinhLuan);
    }

    // Delete comment
    public void deleteBinhLuan(Long id) {
        if (!binhLuanRepository.existsById(id)) {
            throw new RuntimeException("Bình luận không tồn tại với ID: " + id);
        }
        binhLuanRepository.deleteById(id);
    }

    // Approve comment (change status to active)
    public BinhLuanResponseDto approveBinhLuan(Long id) {
        BinhLuan binhLuan = binhLuanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại với ID: " + id));

        binhLuan.setTrangThai("active");
        BinhLuan approvedBinhLuan = binhLuanRepository.save(binhLuan);
        return binhLuanMapper.toResponseDto(approvedBinhLuan);
    }

    // Reject comment (change status to rejected)
    public BinhLuanResponseDto rejectBinhLuan(Long id) {
        BinhLuan binhLuan = binhLuanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại với ID: " + id));

        binhLuan.setTrangThai("rejected");
        BinhLuan rejectedBinhLuan = binhLuanRepository.save(binhLuan);
        return binhLuanMapper.toResponseDto(rejectedBinhLuan);
    }

    // Get comment statistics
    public long countBinhLuanByBaiVietId(Long baiVietId) {
        return binhLuanRepository.countByBaiVietIdAndTrangThai(baiVietId, "active");
    }

    public long countBinhLuanByNguoiDungId(Long nguoiDungId) {
        return binhLuanRepository.countByNguoiDungId(nguoiDungId);
    }
}
