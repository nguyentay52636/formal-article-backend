package org.example.dto.request.generatedCv;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO để tạo CV từ template + AI prompt
 * Chỉ cần 3 field: userId, templateId, prompt
 */
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
     * Prompt mô tả yêu cầu CV từ người dùng
     * VD: "Tạo CV cho vị trí Software Developer với 3 năm kinh nghiệm Java"
     */
    @NotBlank(message = "Prompt không được để trống")
    private String prompt;
    
    /**
     * Tiêu đề CV (optional - nếu không có sẽ do AI tạo)
     */
    private String title;
}
