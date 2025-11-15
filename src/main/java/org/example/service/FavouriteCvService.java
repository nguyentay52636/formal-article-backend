package org.example.service;

import org.example.dto.request.favouriteCv.FavouriteCvCreateRequest;
import org.example.dto.request.favouriteCv.FavouriteCvUpdateRequest;
import org.example.dto.response.favouriteCv.FavouriteCvResponse;
import org.example.entity.FavouriteCv;
import org.example.entity.Template;
import org.example.entity.User;
import org.example.mapper.FavouriteCvMapper;
import org.example.repository.FavouriteCvRepository;
import org.example.repository.TemplateRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavouriteCvService {
    
    @Autowired
    private FavouriteCvRepository favouriteCvRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TemplateRepository templateRepository;
    
    @Autowired
    private FavouriteCvMapper favouriteCvMapper;
    
    /**
     * Lấy FavouriteCv theo ID
     */
    public FavouriteCvResponse getFavouriteCvById(Long id) {
        Optional<FavouriteCv> favouriteCv = favouriteCvRepository.findById(id);
        return favouriteCv.map(favouriteCvMapper::toFavouriteCvResponse)
                          .orElse(null);
    }
    
    /**
     * Lấy tất cả FavouriteCv
     */
    public List<FavouriteCvResponse> getAllFavouriteCv() {
        List<FavouriteCv> favouriteCvs = favouriteCvRepository.findAll();
        return favouriteCvs.stream()
                           .map(favouriteCvMapper::toFavouriteCvResponse)
                           .collect(Collectors.toList());
    }
    
    /**
     * Tạo FavouriteCv mới (thêm CV vào danh sách yêu thích)
     */
    public FavouriteCvResponse createFavouriteCv(FavouriteCvCreateRequest request) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại với ID: " + request.getUserId()));
        
        // Kiểm tra template có tồn tại không
        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template không tồn tại với ID: " + request.getTemplateId()));
        
        // Kiểm tra đã yêu thích chưa
        if (favouriteCvRepository.existsByUserIdAndTemplateId(request.getUserId(), request.getTemplateId())) {
            throw new RuntimeException("CV đã được thêm vào danh sách yêu thích");
        }
        
        // Tạo FavouriteCv mới
        FavouriteCv favouriteCv = favouriteCvMapper.toFavouriteCv(request, user, template);
        
        // Lưu vào database
        FavouriteCv savedFavouriteCv = favouriteCvRepository.save(favouriteCv);
        
        return favouriteCvMapper.toFavouriteCvResponse(savedFavouriteCv);
    }
    
    /**
     * Cập nhật FavouriteCv
     */
    public FavouriteCvResponse updateFavouriteCv(Long id, FavouriteCvUpdateRequest request) {
        // Kiểm tra FavouriteCv có tồn tại không
        FavouriteCv favouriteCv = favouriteCvRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FavouriteCv không tồn tại với ID: " + id));
        
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại với ID: " + request.getUserId()));
        
        // Kiểm tra template có tồn tại không
        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template không tồn tại với ID: " + request.getTemplateId()));
        
        // Kiểm tra nếu cặp user-template mới đã tồn tại (trừ chính record hiện tại)
        Optional<FavouriteCv> existingFavouriteCv = favouriteCvRepository.findByUserIdAndTemplateId(
                request.getUserId(), request.getTemplateId());
        if (existingFavouriteCv.isPresent() && !existingFavouriteCv.get().getId().equals(id)) {
            throw new RuntimeException("CV đã được thêm vào danh sách yêu thích");
        }
        
        // Cập nhật thông tin
        favouriteCvMapper.updateFavouriteCv(request, favouriteCv, user, template);
        
        // Lưu vào database
        FavouriteCv updatedFavouriteCv = favouriteCvRepository.save(favouriteCv);
        
        return favouriteCvMapper.toFavouriteCvResponse(updatedFavouriteCv);
    }
    
    /**
     * Xóa FavouriteCv (xóa CV khỏi danh sách yêu thích)
     */
    public void deleteFavouriteCv(Long id) {
        // Kiểm tra FavouriteCv có tồn tại không
        if (!favouriteCvRepository.existsById(id)) {
            throw new RuntimeException("FavouriteCv không tồn tại với ID: " + id);
        }
        
        // Xóa FavouriteCv
        favouriteCvRepository.deleteById(id);
    }
    
    /**
     * Lấy tất cả FavouriteCv theo User ID
     */
    public List<FavouriteCvResponse> getFavouriteCvByUserId(Long userId) {
        List<FavouriteCv> favouriteCvs = favouriteCvRepository.findByUserId(userId);
        return favouriteCvs.stream()
                           .map(favouriteCvMapper::toFavouriteCvResponse)
                           .collect(Collectors.toList());
    }
    
    /**
     * Lấy tất cả FavouriteCv theo Template ID
     */
    public List<FavouriteCvResponse> getFavouriteCvByTemplateId(Long templateId) {
        List<FavouriteCv> favouriteCvs = favouriteCvRepository.findByTemplateId(templateId);
        return favouriteCvs.stream()
                           .map(favouriteCvMapper::toFavouriteCvResponse)
                           .collect(Collectors.toList());
    }
}

