package org.example.service;

import org.example.dto.TepTin.TepTinCreateDto;
import org.example.dto.TepTin.TepTinResponseDto;
import org.example.dto.TepTin.TepTinUpdateDto;
import org.example.entity.TepTin;
import org.example.entity.NguoiDung;
import org.example.mapping.TepTinMapper;
import org.example.repository.TepTinRepository;
import org.example.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TepTinService {
    
    @Autowired
    private TepTinRepository tepTinRepository;
    
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private TepTinMapper tepTinMapper;
    
    private static final String UPLOAD_DIR = "uploads/";
    
    // ========== FILE UPLOAD & DOWNLOAD ==========
    
    public TepTinResponseDto uploadFile(MultipartFile file, String moTa, String loaiTep, Long nguoiTaoId) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("File không được để trống");
            }
            
            // Validate user exists
            NguoiDung nguoiTao = nguoiDungRepository.findById(nguoiTaoId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + nguoiTaoId));
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = generateUniqueFilename(originalFilename);
            
            // Create upload directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Save file to disk
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Create TepTin entity
            TepTin tepTin = new TepTin();
            tepTin.setTen(originalFilename);
            tepTin.setDuongDan("/uploads/" + uniqueFilename);
            tepTin.setLoaiTep(loaiTep != null ? loaiTep : determineFileType(fileExtension));
            tepTin.setTenTapTin(uniqueFilename);
            tepTin.setDinhDang(fileExtension);
            tepTin.setKichThuoc(file.getSize());
            tepTin.setDuongDanLuu(filePath.toString());
            tepTin.setMoTa(moTa);
            tepTin.setNguoiTao(nguoiTao);
            tepTin.setTrangThai("active");
            
            // Set image dimensions if it's an image
            if (isImageFile(fileExtension)) {
                // For now, set default dimensions. In real implementation, 
                // you would use image processing libraries to get actual dimensions
                tepTin.setChieuRong(800);
                tepTin.setChieuCao(600);
            }
            
            TepTin savedTepTin = tepTinRepository.save(tepTin);
            return tepTinMapper.toResponseDTO(savedTepTin);
            
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload file: " + e.getMessage());
        }
    }
    
    public byte[] downloadFile(Long id) {
        TepTin tepTin = tepTinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy file với id: " + id));
        
        try {
            Path filePath = Paths.get(tepTin.getDuongDanLuu());
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File không tồn tại trên disk");
            }
            
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file: " + e.getMessage());
        }
    }
    
    public TepTinResponseDto incrementDownloadCount(Long id) {
        TepTin tepTin = tepTinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy file với id: " + id));
        
        // For now, just return the file info. In a real implementation,
        // you would increment a download counter field
        return tepTinMapper.toResponseDTO(tepTin);
    }
    
    // ========== FILE MANAGEMENT ==========
    
    public List<TepTinResponseDto> getAllFiles() {
        return tepTinRepository.findAllOrderByNgayTaoDesc().stream()
                .map(tepTinMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Page<TepTinResponseDto> getAllFilesWithPagination(int page, int size, String loaiTep, Long nguoiTaoId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TepTin> tepTinPage;
        
        if (loaiTep != null && nguoiTaoId != null) {
            List<TepTin> files = tepTinRepository.findByNguoiTaoIdAndLoaiTep(nguoiTaoId, loaiTep);
            // Convert to page manually for now
            tepTinPage = tepTinRepository.findAllOrderByNgayTaoDesc(pageable);
        } else if (loaiTep != null) {
            tepTinPage = tepTinRepository.findByLoaiTep(loaiTep, pageable);
        } else if (nguoiTaoId != null) {
            tepTinPage = tepTinRepository.findByNguoiTaoId(nguoiTaoId, pageable);
        } else {
            tepTinPage = tepTinRepository.findAllOrderByNgayTaoDesc(pageable);
        }
        
        return tepTinPage.map(tepTinMapper::toResponseDTO);
    }
    
    public TepTinResponseDto getFileById(Long id) {
        TepTin tepTin = tepTinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy file với id: " + id));
        return tepTinMapper.toResponseDTO(tepTin);
    }
    
    public TepTinResponseDto updateFile(Long id, TepTinUpdateDto updateDto) {
        TepTin tepTin = tepTinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy file với id: " + id));
        
        // Validate unique constraints if updating
        if (updateDto.getDuongDan() != null && !updateDto.getDuongDan().equals(tepTin.getDuongDan())) {
            if (tepTinRepository.existsByDuongDanAndIdNot(updateDto.getDuongDan(), id)) {
                throw new RuntimeException("Đường dẫn file đã tồn tại: " + updateDto.getDuongDan());
            }
        }
        
        if (updateDto.getTen() != null && !updateDto.getTen().equals(tepTin.getTen())) {
            if (tepTinRepository.existsByTenAndIdNot(updateDto.getTen(), id)) {
                throw new RuntimeException("Tên file đã tồn tại: " + updateDto.getTen());
            }
        }
        
        tepTinMapper.updateEntityFromDto(tepTin, updateDto);
        TepTin updatedTepTin = tepTinRepository.save(tepTin);
        return tepTinMapper.toResponseDTO(updatedTepTin);
    }
    
    public void deleteFile(Long id) {
        TepTin tepTin = tepTinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy file với id: " + id));
        
        try {
            // Delete file from disk
            Path filePath = Paths.get(tepTin.getDuongDanLuu());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            // Log error but continue with database deletion
            System.err.println("Lỗi khi xóa file từ disk: " + e.getMessage());
        }
        
        tepTinRepository.deleteById(id);
    }
    
    // ========== FILE SEARCH & FILTER ==========
    
    public List<TepTinResponseDto> searchFiles(String keyword, String loaiTep) {
        List<TepTin> files;
        
        if (loaiTep != null) {
            files = tepTinRepository.searchByKeywordAndLoaiTep(keyword, loaiTep);
        } else {
            files = tepTinRepository.searchByKeyword(keyword);
        }
        
        return files.stream()
                .map(tepTinMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<TepTinResponseDto> getFilesByUser(Long nguoiTaoId) {
        return tepTinRepository.findByNguoiTaoId(nguoiTaoId).stream()
                .map(tepTinMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<TepTinResponseDto> getFilesByType(String loaiTep) {
        return tepTinRepository.findByLoaiTep(loaiTep).stream()
                .map(tepTinMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    // ========== FILE STATISTICS ==========
    
    public Map<String, Object> getFileStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total files
        long totalFiles = tepTinRepository.count();
        stats.put("totalFiles", totalFiles);
        
        // Files by type
        List<Object[]> typeStats = tepTinRepository.getFileStatisticsByType();
        Map<String, Long> filesByType = new HashMap<>();
        for (Object[] stat : typeStats) {
            filesByType.put((String) stat[0], (Long) stat[1]);
        }
        stats.put("filesByType", filesByType);
        
        return stats;
    }
    
    public Map<String, Object> getFileStatistics(Long id) {
        TepTin tepTin = tepTinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy file với id: " + id));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("fileId", tepTin.getId());
        stats.put("fileName", tepTin.getTen());
        stats.put("fileSize", tepTin.getKichThuoc());
        stats.put("fileType", tepTin.getLoaiTep());
        stats.put("uploadDate", tepTin.getNgayTao());
        stats.put("downloadCount", 0L); // Placeholder
        stats.put("viewCount", 0L); // Placeholder
        
        return stats;
    }
    
    // ========== HELPER METHODS ==========
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
    
    private String generateUniqueFilename(String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = getFileExtension(originalFilename);
        return timestamp + "_" + originalFilename;
    }
    
    private String determineFileType(String extension) {
        if (isImageFile(extension)) {
            return "anh";
        } else if (isDocumentFile(extension)) {
            return "tai_lieu";
        } else {
            return "khac";
        }
    }
    
    private boolean isImageFile(String extension) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
        for (String ext : imageExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isDocumentFile(String extension) {
        String[] docExtensions = {"pdf", "doc", "docx", "txt", "rtf", "odt"};
        for (String ext : docExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
