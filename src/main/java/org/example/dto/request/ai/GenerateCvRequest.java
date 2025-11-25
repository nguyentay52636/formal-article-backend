package org.example.dto.request.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO để tạo CV từ AI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateCvRequest {
    
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
     * VD: "Tạo CV cho vị trí Software Developer với 3 năm kinh nghiệm"
     */
    @NotBlank(message = "Prompt không được để trống")
    private String prompt;
    
    /**
     * Tiêu đề CV (tùy chọn, nếu không có AI sẽ tự tạo)
     */
    private String title;
}
