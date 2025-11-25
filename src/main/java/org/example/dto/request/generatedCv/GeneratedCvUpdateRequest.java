package org.example.dto.request.generatedCv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO để cập nhật CV
 * Tất cả fields đều optional - chỉ update những field được cung cấp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedCvUpdateRequest {
    
    /**
     * Tiêu đề CV
     */
    private String title;
    
    /**
     * Dữ liệu CV từ user (JSON string)
     */
    private String userDataJson;
    
    /**
     * Style configuration (JSON string)
     */
    private String styleJson;
    
    /**
     * HTML output đã render
     */
    private String htmlOutput;
    
    /**
     * ID template mới (nếu muốn đổi template)
     */
    private Long templateId;
    
    /**
     * URL file PDF
     */
    private String pdfUrl;
}
