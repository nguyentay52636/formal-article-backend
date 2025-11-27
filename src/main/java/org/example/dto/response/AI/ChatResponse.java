package org.example.dto.response.AI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response cho Chat API - có thể trả về cả text response và CV preview
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatResponse {
    
    /**
     * Text response từ AI
     */
    private String response;
    
    /**
     * CV preview (nếu user muốn tạo CV)
     */
    private CvPreviewData cvPreview;
    
    /**
     * Model được sử dụng
     */
    private String model;
    
    /**
     * Loại response: "text" hoặc "cv" hoặc "both"
     */
    private String type;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CvPreviewData {
        private String title;
        private JsonNode dataJson;
        private JsonNode styleJson;
        private String htmlOutput;
    }
    
    /**
     * Tạo response chỉ có text
     */
    public static ChatResponse textOnly(String response, String model) {
        return ChatResponse.builder()
                .response(response)
                .model(model)
                .type("text")
                .build();
    }
    
    /**
     * Tạo response có cả text và CV preview
     */
    public static ChatResponse withCv(String textResponse, CvPreviewData cvData, String model) {
        return ChatResponse.builder()
                .response(textResponse)
                .cvPreview(cvData)
                .model(model)
                .type("both")
                .build();
    }
    
    /**
     * Tạo response chỉ có CV preview
     */
    public static ChatResponse cvOnly(CvPreviewData cvData, String model) {
        return ChatResponse.builder()
                .cvPreview(cvData)
                .model(model)
                .type("cv")
                .build();
    }
}
