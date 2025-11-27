package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.CvPromptTemplate;
import org.example.dto.request.generatedCv.GeneratedCvCreateRequest;
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

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service quản lý CRUD cho GeneratedCv
 * Sử dụng AI để generate nội dung CV từ template + prompt
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratedCvManagementService {

    private final GeneratedCvRepository generatedCvRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final GeneratedCvMapper generatedCvMapper;
    private final RobustChatService robustChatService;

    // ==================== CREATE ====================

    /**
     * Tạo CV mới - hỗ trợ 2 mode:
     * 1. AI Mode: prompt → AI generate dataJson, styleJson, htmlOutput
     * 2. Manual Mode: user tự nhập dataJson, styleJson, htmlOutput
     */
    @Transactional
    public GeneratedCvResponse createCV(GeneratedCvCreateRequest request) {
        // 1. Validate request
        if (!request.isValid()) {
            throw new IllegalArgumentException("Request phải có prompt (AI mode) hoặc dataJson (Manual mode)");
        }
        
        // 2. Validate entities
        User user = findUserById(request.getUserId());
        Template template = findTemplateById(request.getTemplateId());
        
        // 3. Tạo entity
        GeneratedCv entity = new GeneratedCv();
        entity.setUser(user);
        entity.setTemplate(template);
        
        if (request.isAiMode()) {
            // AI Mode: Gọi AI để generate CV data
            createCvWithAI(entity, request, template);
        } else {
            // Manual Mode: Lấy data từ request
            createCvManual(entity, request);
        }
        
        // 4. Lưu và trả về
        GeneratedCv savedEntity = generatedCvRepository.save(entity);
        log.info("Created CV [{}] with ID: {} for user: {} using template: {}", 
                request.isAiMode() ? "AI" : "Manual",
                savedEntity.getId(), user.getId(), template.getId());
        
        return generatedCvMapper.toResponse(savedEntity);
    }
    
    /**
     * Tạo CV với AI mode
     */
    private void createCvWithAI(GeneratedCv entity, GeneratedCvCreateRequest request, Template template) {
        ParsedCvResult aiResult = generateCVDataFromAI(request.getPrompt(), template);
        
        if (aiResult == null || !aiResult.isValid()) {
            throw new GeneratedCvService.CvGenerationException("Lỗi khi tạo CV từ AI. Vui lòng thử lại.");
        }
        
        // Xác định title (ưu tiên user input)
        String title = (request.getTitle() != null && !request.getTitle().isBlank()) 
                ? request.getTitle() 
                : aiResult.title();
        
        entity.setTitle(title);
        entity.setDataJson(aiResult.dataJson());
        entity.setStyleJson(aiResult.styleJson());
        entity.setHtmlOutput(aiResult.htmlOutput());
    }
    
    /**
     * Tạo CV với Manual mode
     */
    private void createCvManual(GeneratedCv entity, GeneratedCvCreateRequest request) {
        // Title: ưu tiên user input, nếu không có thì dùng default
        String title = (request.getTitle() != null && !request.getTitle().isBlank()) 
                ? request.getTitle() 
                : "CV - " + System.currentTimeMillis();
        
        entity.setTitle(title);
        entity.setDataJson(request.getDataJson());
        entity.setStyleJson(request.getStyleJson() != null ? request.getStyleJson() : "{}");
        entity.setHtmlOutput(request.getHtmlOutput() != null ? request.getHtmlOutput() : "");
    }

    // ==================== READ ====================

    /**
     * Lấy CV theo ID
     */
    @Transactional(readOnly = true)
    public GeneratedCvResponse getCVById(Long id) {
        GeneratedCv entity = findCvById(id);
        return generatedCvMapper.toResponse(entity);
    }

    /**
     * Lấy tất cả CV (không phân trang)
     */
    @Transactional(readOnly = true)
    public List<GeneratedCvResponse> getAllCVs() {
        List<GeneratedCv> cvList = generatedCvRepository.findAllWithUserAndTemplate();
        return cvList.stream()
                .map(generatedCvMapper::toResponse)
                .toList();
    }

    /**
     * Lấy tất cả CV của user (không phân trang)
     */
    @Transactional(readOnly = true)
    public List<GeneratedCvResponse> getAllCVsByUserId(Long userId) {
        List<GeneratedCv> cvList = generatedCvRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return cvList.stream()
                .map(generatedCvMapper::toResponse)
                .toList();
    }

    // ==================== UPDATE ====================

    /**
     * Cập nhật CV - hỗ trợ 2 mode:
     * 1. AI Mode: có prompt mới → regenerate từ AI
     * 2. Manual Mode: có dataJson → cập nhật trực tiếp
     * 3. Chỉ update title nếu không có cả 2
     */
    @Transactional
    public GeneratedCvResponse updateCV(Long id, GeneratedCvCreateRequest request) {
        GeneratedCv entity = findCvById(id);
        Template template = entity.getTemplate();
        
        // Nếu đổi template
        if (request.getTemplateId() != null && !request.getTemplateId().equals(template.getId())) {
            template = findTemplateById(request.getTemplateId());
            entity.setTemplate(template);
        }
        
        if (request.isAiMode()) {
            // AI Mode: Regenerate từ AI
            updateCvWithAI(entity, request, template);
        } else if (request.isManualMode()) {
            // Manual Mode: Cập nhật data trực tiếp
            updateCvManual(entity, request);
        } else {
            // Chỉ update title
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                entity.setTitle(request.getTitle());
            }
        }
        
        GeneratedCv savedEntity = generatedCvRepository.save(entity);
        log.info("Updated CV [{}] with ID: {}", 
                request.isAiMode() ? "AI" : (request.isManualMode() ? "Manual" : "Title only"), id);
        
        return generatedCvMapper.toResponse(savedEntity);
    }
    
    /**
     * Cập nhật CV với AI mode
     */
    private void updateCvWithAI(GeneratedCv entity, GeneratedCvCreateRequest request, Template template) {
        ParsedCvResult aiResult = generateCVDataFromAI(request.getPrompt(), template);
        
        if (aiResult != null && aiResult.isValid()) {
            entity.setDataJson(aiResult.dataJson());
            entity.setStyleJson(aiResult.styleJson());
            entity.setHtmlOutput(aiResult.htmlOutput());
            
            // Update title
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                entity.setTitle(request.getTitle());
            } else {
                entity.setTitle(aiResult.title());
            }
        }
    }
    
    /**
     * Cập nhật CV với Manual mode
     */
    private void updateCvManual(GeneratedCv entity, GeneratedCvCreateRequest request) {
        // Update title nếu có
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            entity.setTitle(request.getTitle());
        }
        
        // Update dataJson
        if (request.getDataJson() != null) {
            entity.setDataJson(request.getDataJson());
        }
        
        // Update styleJson
        if (request.getStyleJson() != null) {
            entity.setStyleJson(request.getStyleJson());
        }
        
        // Update htmlOutput
        if (request.getHtmlOutput() != null) {
            entity.setHtmlOutput(request.getHtmlOutput());
        }
    }

    // ==================== DELETE ====================

    /**
     * Xóa CV
     */
    @Transactional
    public void deleteCV(Long id) {
        GeneratedCv entity = findCvById(id);
        generatedCvRepository.delete(entity);
        log.info("Deleted CV with ID: {}", id);
    }

    // ==================== AI GENERATION ====================

    /**
     * Gọi AI để generate CV data từ prompt + template info
     */
    private ParsedCvResult generateCVDataFromAI(String userPrompt, Template template) {
        // Tạo prompt với context từ template
        String enhancedPrompt = buildPromptWithTemplateContext(userPrompt, template);
        String prompt = CvPromptTemplate.getPrompt(enhancedPrompt);
        
        log.debug("Calling AI to generate CV data...");
        RobustChatService.ResponseWithModel aiResponse = robustChatService.askAIWithModel(prompt);
        
        log.debug("AI response from model: {}", aiResponse.getModel());
        return AICvResponseParser.parse(aiResponse.getContent());
    }

    /**
     * Tạo prompt với context từ template (style, design info)
     */
    private String buildPromptWithTemplateContext(String userPrompt, Template template) {
        StringBuilder sb = new StringBuilder();
        sb.append(userPrompt);
        
        // Thêm context từ template
        sb.append("\n\n--- TEMPLATE CONTEXT ---");
        sb.append("\nTên template: ").append(template.getName());
        
        if (template.getDesign() != null) {
            sb.append("\nPhong cách thiết kế: ").append(template.getDesign());
        }
        if (template.getColor() != null) {
            sb.append("\nMàu chủ đạo: ").append(template.getColor());
        }
        if (template.getLanguage() != null) {
            sb.append("\nNgôn ngữ: ").append(template.getLanguage());
        }
        if (template.getUsage() != null) {
            sb.append("\nLĩnh vực sử dụng: ").append(template.getUsage());
        }
        
        sb.append("\n\nHãy tạo CV phù hợp với phong cách template trên.");
        
        return sb.toString();
    }

    // ==================== ENTITY FINDERS ====================

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new GeneratedCvService.UserNotFoundException(id));
    }

    private Template findTemplateById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new GeneratedCvService.TemplateNotFoundException(id));
    }

    private GeneratedCv findCvById(Long id) {
        return generatedCvRepository.findById(id)
                .orElseThrow(() -> new GeneratedCvService.CvNotFoundException(id));
    }
}
