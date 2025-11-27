package org.example.dto.request.generatedCv;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedCvCreateRequest {
    
    /**
     * ID của user tạo CV
     */
    @NotNull(message = "User ID không được để trống")
    private Long userId;
    
    /**
     * ID của template sử dụng
     */
    @NotNull(message = "Template ID không được để trống")
    private Long templateId;
    
    /**
     * Tiêu đề CV (optional - nếu không có sẽ do AI tạo hoặc dùng default)
     */
    private String title;
    
    // ==================== AI MODE ====================
    
    /**
     * Prompt mô tả yêu cầu CV từ người dùng (cho AI mode)
     * VD: "Tạo CV cho vị trí Software Developer với 3 năm kinh nghiệm Java"
     */
    private String prompt;
    
    // ==================== MANUAL MODE ====================
    
    /**
     * Dữ liệu CV thô dạng JSON string (cho manual mode)
     * Chứa: personalInfo, summary, education, experience, projects, skills, languages
     */
    private String dataJson;
    
    /**
     * Cấu hình style dạng JSON string (cho manual mode)
     * Chứa: font, color, layout, sectionOrder
     */
    private String styleJson;
    
    /**
     * HTML output đã render (cho manual mode)
     */
    private String htmlOutput;
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Kiểm tra request có phải AI mode không
     * AI mode: có prompt và không có dataJson
     */
    public boolean isAiMode() {
        return prompt != null && !prompt.isBlank() && (dataJson == null || dataJson.isBlank());
    }
    
    /**
     * Kiểm tra request có phải Manual mode không
     * Manual mode: có dataJson
     */
    public boolean isManualMode() {
        return dataJson != null && !dataJson.isBlank();
    }
    
    /**
     * Validate request - phải là 1 trong 2 mode
     */
    public boolean isValid() {
        return isAiMode() || isManualMode();
    }
}
