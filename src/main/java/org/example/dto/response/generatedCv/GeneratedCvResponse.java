package org.example.dto.response.generatedCv;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.template.TemplateResponse;
import org.example.dto.response.user.UserResponse;

import java.time.LocalDateTime;

/**
 * DTO Response cho GeneratedCv entity
 * Chứa thông tin CV đã được tạo từ AI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratedCvResponse {

    private Long id;
    
    private UserResponse user;
    
    private TemplateResponse template;
    
    /**
     * Tiêu đề CV
     */
    private String title;
    
    /**
     * Dữ liệu CV thô dạng JSON string
     * Chứa thông tin cá nhân, kỹ năng, học vấn, kinh nghiệm...
     */
    private String dataJson;
    
    /**
     * Cấu hình định dạng dạng JSON string
     * Chứa font, màu chủ đạo, bố cục, thứ tự section...
     */
    private String styleJson;
    
    /**
     * HTML hoàn chỉnh để FE render CV
     */
    private String htmlOutput;
    
    /**
     * URL file PDF (nếu có)
     */
    private String pdfUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
