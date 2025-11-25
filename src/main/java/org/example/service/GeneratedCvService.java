package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.CvPromptTemplate;
import org.example.dto.request.ai.GenerateCvRequest;
import org.example.dto.response.generatedCv.GeneratedCvPreviewResponse;
import org.example.dto.response.generatedCv.GeneratedCvResponse;
import org.example.entity.GeneratedCv;
import org.example.entity.Template;
import org.example.entity.User;
import org.example.mapper.GeneratedCvMapper;
import org.example.repository.GeneratedCvRepository;
import org.example.repository.TemplateRepository;
import org.example.repository.UserRepository;
import org.example.util.AICvResponseParser;
import org.example.util.AICvResponseParser.ParsedCvResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic tạo và quản lý CV được generate từ AI
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratedCvService {

    private final GeneratedCvRepository generatedCvRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final RobustChatService robustChatService;
    private final GeneratedCvMapper generatedCvMapper;

    // ==================== CV GENERATION ====================

    /**
     * Tạo CV từ AI và lưu vào database
     */
    @Transactional
    public GeneratedCvResponse generateAndSaveCV(GenerateCvRequest request) {
        // 1. Validate entities
        User user = findUserById(request.getUserId());
        Template template = findTemplateById(request.getTemplateId());
        
        // 2. Generate CV từ AI
        ParsedCvResult aiResult = generateFromAI(request.getPrompt());
        if (aiResult == null) {
            throw new CvGenerationException("Lỗi khi tạo CV từ AI");
        }
        
        // 3. Xác định title (ưu tiên user input)
        String title = resolveTitle(request.getTitle(), aiResult.title());
        
        // 4. Lưu entity
        GeneratedCv entity = generatedCvMapper.toEntity(
                title,
                aiResult.dataJson(),
                aiResult.styleJson(),
                aiResult.htmlOutput(),
                user,
                template
        );
        
        GeneratedCv savedEntity = generatedCvRepository.save(entity);
        log.info("Created CV with ID: {} for user: {}", savedEntity.getId(), user.getId());
        
        return generatedCvMapper.toResponse(savedEntity);
    }

    /**
     * Tạo CV preview từ AI (không lưu database)
     */
    public GeneratedCvPreviewResponse generateCVPreview(String prompt) {
        try {
            ParsedCvResult aiResult = generateFromAI(prompt);
            
            if (aiResult == null || !aiResult.isValid()) {
                return GeneratedCvPreviewResponse.error("Lỗi khi tạo CV từ AI");
            }
            
            return GeneratedCvPreviewResponse.success(
                    aiResult.title(),
                    aiResult.dataJsonNode(),
                    aiResult.styleJsonNode(),
                    aiResult.htmlOutput()
            );
            
        } catch (Exception e) {
            log.error("Error generating CV preview: {}", e.getMessage());
            return GeneratedCvPreviewResponse.error("Lỗi: " + e.getMessage());
        }
    }

    // ==================== CRUD OPERATIONS ====================

    /**
     * Lấy CV theo ID
     */
    public GeneratedCvResponse getCVById(Long id) {
        GeneratedCv entity = findCvById(id);
        return generatedCvMapper.toResponse(entity);
    }

    /**
     * Lấy tất cả CV của một user
     */
    public List<GeneratedCvResponse> getCVsByUserId(Long userId) {
        return generatedCvRepository.findByUserId(userId)
                .stream()
                .map(generatedCvMapper::toResponse)
                .toList();
    }

    /**
     * Cập nhật CV
     */
    @Transactional
    public GeneratedCvResponse updateCV(Long id, GeneratedCvResponse updateRequest) {
        GeneratedCv entity = findCvById(id);
        
        generatedCvMapper.updateEntity(
                entity,
                updateRequest.getTitle(),
                updateRequest.getDataJson(),
                updateRequest.getStyleJson(),
                updateRequest.getHtmlOutput(),
                updateRequest.getPdfUrl()
        );
        
        GeneratedCv savedEntity = generatedCvRepository.save(entity);
        log.info("Updated CV with ID: {}", id);
        
        return generatedCvMapper.toResponse(savedEntity);
    }

    /**
     * Xóa CV
     */
    @Transactional
    public void deleteCV(Long id) {
        if (!generatedCvRepository.existsById(id)) {
            throw new CvNotFoundException(id);
        }
        generatedCvRepository.deleteById(id);
        log.info("Deleted CV with ID: {}", id);
    }

    /**
     * Lưu CV từ preview
     */
    @Transactional
    public GeneratedCvResponse savePreviewCV(Long userId, Long templateId, GeneratedCvResponse previewResponse) {
        User user = findUserById(userId);
        Template template = findTemplateById(templateId);
        
        GeneratedCv entity = generatedCvMapper.toEntity(
                previewResponse.getTitle(),
                previewResponse.getDataJson(),
                previewResponse.getStyleJson(),
                previewResponse.getHtmlOutput(),
                user,
                template
        );
        
        GeneratedCv savedEntity = generatedCvRepository.save(entity);
        log.info("Saved preview CV with ID: {} for user: {}", savedEntity.getId(), userId);
        
        return generatedCvMapper.toResponse(savedEntity);
    }

    // ==================== PRIVATE HELPERS ====================

    /**
     * Gọi AI để generate CV data
     */
    private ParsedCvResult generateFromAI(String userPrompt) {
        String prompt = CvPromptTemplate.getPrompt(userPrompt);
        
        log.debug("Calling AI with prompt length: {}", prompt.length());
        RobustChatService.ResponseWithModel aiResponse = robustChatService.askAIWithModel(prompt);
        
        log.debug("AI response from model: {}", aiResponse.getModel());
        return AICvResponseParser.parse(aiResponse.getContent());
    }

    /**
     * Resolve title - ưu tiên user input
     */
    private String resolveTitle(String userTitle, String aiTitle) {
        if (userTitle != null && !userTitle.isBlank()) {
            return userTitle;
        }
        return aiTitle != null ? aiTitle : "CV";
    }

    // ==================== ENTITY FINDERS ====================

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private Template findTemplateById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new TemplateNotFoundException(id));
    }

    private GeneratedCv findCvById(Long id) {
        return generatedCvRepository.findById(id)
                .orElseThrow(() -> new CvNotFoundException(id));
    }

    // ==================== CUSTOM EXCEPTIONS ====================

    public static class CvGenerationException extends RuntimeException {
        public CvGenerationException(String message) {
            super(message);
        }
    }

    public static class CvNotFoundException extends RuntimeException {
        public CvNotFoundException(Long id) {
            super("CV không tồn tại với ID: " + id);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(Long id) {
            super("User không tồn tại với ID: " + id);
        }
    }

    public static class TemplateNotFoundException extends RuntimeException {
        public TemplateNotFoundException(Long id) {
            super("Template không tồn tại với ID: " + id);
        }
    }
}
