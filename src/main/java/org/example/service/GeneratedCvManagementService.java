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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
     * Tạo CV mới từ template + AI prompt
     * Flow: 
     * 1. Lấy Template (html, css, style)
     * 2. Gọi AI để generate dataJson từ prompt
     * 3. Kết hợp template + dataJson để tạo htmlOutput
     * 4. Lưu GeneratedCv
     */
    @Transactional
    public GeneratedCvResponse createCV(GeneratedCvCreateRequest request) {
        // 1. Validate entities
        User user = findUserById(request.getUserId());
        Template template = findTemplateById(request.getTemplateId());
        
        // 2. Gọi AI để generate CV data từ prompt
        ParsedCvResult aiResult = generateCVDataFromAI(request.getPrompt(), template);
        
        if (aiResult == null || !aiResult.isValid()) {
            throw new GeneratedCvService.CvGenerationException("Lỗi khi tạo CV từ AI. Vui lòng thử lại.");
        }
        
        // 3. Xác định title (ưu tiên user input)
        String title = (request.getTitle() != null && !request.getTitle().isBlank()) 
                ? request.getTitle() 
                : aiResult.title();
        
        // 4. Tạo entity
        GeneratedCv entity = new GeneratedCv();
        entity.setUser(user);
        entity.setTemplate(template);
        entity.setTitle(title);
        entity.setDataJson(aiResult.dataJson());
        entity.setStyleJson(aiResult.styleJson());
        entity.setHtmlOutput(aiResult.htmlOutput());
        
        // 5. Lưu và trả về
        GeneratedCv savedEntity = generatedCvRepository.save(entity);
        log.info("Created CV with ID: {} for user: {} using template: {}", 
                savedEntity.getId(), user.getId(), template.getId());
        
        return generatedCvMapper.toResponse(savedEntity);
    }

    // ==================== READ ====================

    /**
     * Lấy CV theo ID
     */
    public GeneratedCvResponse getCVById(Long id) {
        GeneratedCv entity = findCvById(id);
        return generatedCvMapper.toResponse(entity);
    }

    /**
     * Lấy danh sách CV của user (có phân trang và tìm kiếm)
     */
    public Page<GeneratedCvResponse> getCVsByUserId(Long userId, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<GeneratedCv> cvPage;
        if (keyword != null && !keyword.isBlank()) {
            cvPage = generatedCvRepository.searchByUserIdAndTitle(userId, keyword.trim(), pageable);
        } else {
            cvPage = generatedCvRepository.findByUserId(userId, pageable);
        }
        
        return cvPage.map(generatedCvMapper::toResponse);
    }

    // ==================== UPDATE ====================

    /**
     * Cập nhật CV - có thể regenerate từ prompt mới
     */
    @Transactional
    public GeneratedCvResponse updateCV(Long id, GeneratedCvCreateRequest request) {
        GeneratedCv entity = findCvById(id);
        Template template = entity.getTemplate();
        
        // Nếu có prompt mới, regenerate content
        if (request.getPrompt() != null && !request.getPrompt().isBlank()) {
            // Nếu đổi template
            if (request.getTemplateId() != null && !request.getTemplateId().equals(template.getId())) {
                template = findTemplateById(request.getTemplateId());
                entity.setTemplate(template);
            }
            
            ParsedCvResult aiResult = generateCVDataFromAI(request.getPrompt(), template);
            if (aiResult != null && aiResult.isValid()) {
                entity.setDataJson(aiResult.dataJson());
                entity.setStyleJson(aiResult.styleJson());
                entity.setHtmlOutput(aiResult.htmlOutput());
                
                // Update title nếu có
                if (request.getTitle() != null && !request.getTitle().isBlank()) {
                    entity.setTitle(request.getTitle());
                } else {
                    entity.setTitle(aiResult.title());
                }
            }
        } else {
            // Chỉ update title nếu không có prompt mới
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                entity.setTitle(request.getTitle());
            }
        }
        
        GeneratedCv savedEntity = generatedCvRepository.save(entity);
        log.info("Updated CV with ID: {}", id);
        
        return generatedCvMapper.toResponse(savedEntity);
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
