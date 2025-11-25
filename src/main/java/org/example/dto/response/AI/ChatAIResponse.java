package org.example.dto.response.AI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAIResponse {
    
    private String content;  // HTML formatted content
    private String rawContent;  // Original markdown content
    private String model;  // Model used for this response
    private boolean formatted;  // Whether content is formatted
    
    public static ChatAIResponse of(String rawContent, String model) {
        return ChatAIResponse.builder()
                .rawContent(rawContent)
                .model(model)
                .formatted(false)
                .build();
    }
    
    public static ChatAIResponse formatted(String htmlContent, String rawContent, String model) {
        return ChatAIResponse.builder()
                .content(htmlContent)
                .rawContent(rawContent)
                .model(model)
                .formatted(true)
                .build();
    }
}
