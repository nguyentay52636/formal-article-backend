package org.example.dto.response.generatedCv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO Response cho CV Preview từ AI
 * dataJson và styleJson là Object (không phải String)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratedCvPreviewResponse {

    /**
     * Trạng thái: success / error
     */
    private String status;
    
    /**
     * Tiêu đề CV
     */
    private String title;
    
    /**
     * Dữ liệu CV thô dạng JSON object
     * Chứa: personalInfo, summary, education, experience, projects, skills, languages
     */
    private JsonNode dataJson;
    
    /**
     * Cấu hình định dạng dạng JSON object
     * Chứa: font, color, sectionOrder, layout
     */
    private JsonNode styleJson;
    
    /**
     * HTML hoàn chỉnh để FE render CV
     */
    private String htmlOutput;
    
    /**
     * URL file PDF (nếu có)
     */
    private String pdfUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updatedAt;
    
    /**
     * Thông báo lỗi (chỉ khi status = error)
     */
    private String errorMessage;
    
    /**
     * Tạo response thành công
     */
    public static GeneratedCvPreviewResponse success(String title, JsonNode dataJson, JsonNode styleJson, String htmlOutput) {
        return GeneratedCvPreviewResponse.builder()
                .status("success")
                .title(title)
                .dataJson(dataJson)
                .styleJson(styleJson)
                .htmlOutput(htmlOutput)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Tạo response lỗi
     */
    public static GeneratedCvPreviewResponse error(String errorMessage) {
        return GeneratedCvPreviewResponse.builder()
                .status("error")
                .errorMessage(errorMessage)
                .build();
    }
}
