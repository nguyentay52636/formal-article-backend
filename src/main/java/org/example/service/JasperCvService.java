package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.example.entity.GeneratedCv;
import org.example.entity.Template;
import org.example.repository.GeneratedCvRepository;
import org.example.util.JasperCvUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Service xử lý render PDF cho CV sử dụng JasperReports
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JasperCvService {

    private final GeneratedCvRepository generatedCvRepository;
    private final ObjectMapper objectMapper;

    /**
     * Preview PDF - render tạm từ CV data, trả về byte[]
     * Không lưu file, chỉ để FE xem trực tiếp
     */
    public byte[] previewPdf(Long cvId) {
        GeneratedCv cv = findCvById(cvId);
        
        try {
            Map<String, Object> data = buildDataMap(cv);
            String templateName = getTemplateName(cv.getTemplate());
            
            return JasperCvUtil.generatePdf(data, templateName);
            
        } catch (JRException e) {
            log.error("Error generating PDF preview for CV {}: {}", cvId, e.getMessage());
            throw new RuntimeException("Lỗi khi render PDF preview: " + e.getMessage());
        }
    }

    /**
     * Render PDF thật và lưu vào storage
     * Cập nhật pdfUrl trong GeneratedCv
     */
    @Transactional
    public String savePdf(Long cvId) {
        GeneratedCv cv = findCvById(cvId);
        
        try {
            Map<String, Object> data = buildDataMap(cv);
            String templateName = getTemplateName(cv.getTemplate());
            
            // Render JasperPrint
            JasperPrint jasperPrint = JasperCvUtil.generateJasperPrint(data, templateName);
            
            // Xóa PDF cũ nếu có
            if (cv.getPdfUrl() != null && !cv.getPdfUrl().isBlank()) {
                JasperCvUtil.deletePdfFile(cv.getPdfUrl());
            }
            
            // Lưu PDF mới
            String pdfUrl = JasperCvUtil.savePdfToFile(jasperPrint, cvId);
            
            // Cập nhật entity
            cv.setPdfUrl(pdfUrl);
            generatedCvRepository.save(cv);
            
            log.info("PDF saved for CV {}: {}", cvId, pdfUrl);
            return pdfUrl;
            
        } catch (JRException e) {
            log.error("Error saving PDF for CV {}: {}", cvId, e.getMessage());
            throw new RuntimeException("Lỗi khi lưu PDF: " + e.getMessage());
        }
    }

    /**
     * Lấy PDF đã lưu để download
     * Trả về file path hoặc null nếu chưa có
     */
    public String getPdfPath(Long cvId) {
        GeneratedCv cv = findCvById(cvId);
        
        if (cv.getPdfUrl() == null || cv.getPdfUrl().isBlank()) {
            return null;
        }
        
        return "uploads" + cv.getPdfUrl();
    }

    /**
     * Xóa PDF file khi xóa CV
     */
    public void deletePdf(Long cvId) {
        GeneratedCv cv = generatedCvRepository.findById(cvId).orElse(null);
        
        if (cv != null && cv.getPdfUrl() != null) {
            JasperCvUtil.deletePdfFile(cv.getPdfUrl());
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Build data map từ GeneratedCv để truyền vào Jasper template
     */
    private Map<String, Object> buildDataMap(GeneratedCv cv) {
        Map<String, Object> data = new HashMap<>();
        
        // Basic info
        data.put("title", cv.getTitle());
        data.put("cvId", cv.getId());
        
        // Parse dataJson thành Map
        if (cv.getDataJson() != null && !cv.getDataJson().isBlank()) {
            try {
                Map<String, Object> cvData = objectMapper.readValue(
                    cv.getDataJson(), 
                    new TypeReference<Map<String, Object>>() {}
                );
                data.putAll(cvData);
            } catch (Exception e) {
                log.warn("Error parsing dataJson for CV {}: {}", cv.getId(), e.getMessage());
            }
        }
        
        // Parse styleJson
        if (cv.getStyleJson() != null && !cv.getStyleJson().isBlank()) {
            try {
                Map<String, Object> styleData = objectMapper.readValue(
                    cv.getStyleJson(), 
                    new TypeReference<Map<String, Object>>() {}
                );
                data.put("style", styleData);
            } catch (Exception e) {
                log.warn("Error parsing styleJson for CV {}: {}", cv.getId(), e.getMessage());
            }
        }
        
        // Template info
        if (cv.getTemplate() != null) {
            data.put("templateName", cv.getTemplate().getName());
            data.put("templateDesign", cv.getTemplate().getDesign());
        }
        
        // User info
        if (cv.getUser() != null) {
            data.put("userId", cv.getUser().getId());
            data.put("userEmail", cv.getUser().getEmail());
        }
        
        return data;
    }

    /**
     * Lấy tên template Jasper từ Template entity
     * Luôn kiểm tra file .jrxml có tồn tại không, nếu không thì dùng default_cv
     */
    private String getTemplateName(Template template) {
        if (template == null) {
            return "default_cv";
        }
        
        // Thử slug trước
        if (template.getSlug() != null && !template.getSlug().isBlank()) {
            if (templateExists(template.getSlug())) {
                return template.getSlug();
            }
        }
        
        // Thử design
        if (template.getDesign() != null && !template.getDesign().isBlank()) {
            String designName = template.getDesign().toLowerCase().replace(" ", "_");
            if (templateExists(designName)) {
                return designName;
            }
        }
        
        // Fallback về default
        log.info("Jasper template not found for template ID {}, using default_cv", template.getId());
        return "default_cv";
    }
    
    /**
     * Kiểm tra file .jrxml có tồn tại trong resources không
     */
    private boolean templateExists(String templateName) {
        String path = "/jasper/" + templateName + ".jrxml";
        return getClass().getResourceAsStream(path) != null;
    }

    private GeneratedCv findCvById(Long id) {
        return generatedCvRepository.findById(id)
                .orElseThrow(() -> new GeneratedCvService.CvNotFoundException(id));
    }
}
