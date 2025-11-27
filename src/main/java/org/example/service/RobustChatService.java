package org.example.service;

import org.example.dto.request.ai.OpenRouterRequest;
import org.example.dto.request.ai.OpenRouterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RobustChatService {

    private final RestTemplate restTemplate;
    
    @Value("${openrouter.api.key}")
    private String openRouterApiKey;
    
    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";
    
    // Sắp xếp lại models: ưu tiên các model nhanh và free trước
    // Các model nhỏ hơn thường nhanh hơn và đủ tốt cho chat
    private final List<String> models = List.of(
        // Fast và free models - ưu tiên cao nhất
        "google/gemini-flash-1.5-8b:free",           // Rất nhanh
        "qwen/qwen-2.5-7b-instruct:free",            // Nhanh và tốt
        "meta-llama/llama-3.2-3b-instruct:free",     // Rất nhanh
        "deepseek/deepseek-chat-v3-0324:free",       // Nhanh
        "x-ai/grok-4.1-fast:free",                   // Nhanh
        // Models lớn hơn - dự phòng
        "meta-llama/llama-3.3-70b-instruct:free",    // Chậm hơn nhưng tốt hơn
        "openai/gpt-5.1"                             // Premium - dự phòng cuối cùng
    );

    // Giảm delay để retry nhanh hơn
    private static final int DELAY_BETWEEN_RETRIES_MS = 300;

    public RobustChatService() {
        this.restTemplate = new RestTemplate();
    }

    public String askAI(String prompt) {
        return askAIWithModel(prompt).getContent();
    }
    
    public ResponseWithModel askAIWithModel(String prompt) {
        for (String model : models) {
            try {
                long startTime = System.currentTimeMillis();
                
                // Tạo request body với temperature thấp hơn để response nhanh và nhất quán hơn
                List<OpenRouterRequest.Message> messages = new ArrayList<>();
                messages.add(new OpenRouterRequest.Message("user", prompt));
                
                // Giảm temperature từ 0.7 xuống 0.5 để response nhanh hơn và ngắn gọn hơn
                OpenRouterRequest request = new OpenRouterRequest(model, messages, 0.5);
                
                // Tạo headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(openRouterApiKey);
                
                HttpEntity<OpenRouterRequest> entity = new HttpEntity<>(request, headers);
                
                // Gọi API
                ResponseEntity<OpenRouterResponse> response = restTemplate.exchange(
                    OPENROUTER_API_URL,
                    HttpMethod.POST,
                    entity,
                    OpenRouterResponse.class
                );
                
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                // Chỉ log khi debug (có thể tắt trong production)
                if (duration > 2000) { // Chỉ log các request chậm
                    System.out.println("Model " + model + " hoàn thành sau " + duration + "ms");
                }

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    OpenRouterResponse responseBody = response.getBody();
                    
                    // Kiểm tra lỗi từ API
                    if (responseBody.getError() != null) {
                        // Chỉ log lỗi nghiêm trọng
                        if (!responseBody.getError().getMessage().contains("rate limit")) {
                            System.err.println("API error: " + responseBody.getError().getMessage());
                        }
                        continue;
                    }
                    
                    String output = responseBody.getContent();
                    
                    if (output != null && !output.isEmpty() && !output.trim().isEmpty()) {
                        return new ResponseWithModel(output, model);
                    }
                }

            } catch (Exception e) {
                // Chỉ log exception nghiêm trọng, không print stack trace đầy đủ để tăng tốc
                if (!e.getMessage().contains("timeout") && !e.getMessage().contains("rate limit")) {
                    System.err.println("Model " + model + " failed: " + e.getMessage());
                }
                
                // Đợi một chút trước khi thử model tiếp theo
                try {
                    Thread.sleep(DELAY_BETWEEN_RETRIES_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return new ResponseWithModel("Hiện tại hệ thống AI đang bận, vui lòng thử lại sau.", null);
                }
            }
        }
        return new ResponseWithModel("Hiện tại hệ thống AI đang bận, vui lòng thử lại sau.", null);
    }
    
    public static class ResponseWithModel {
        private final String content;
        private final String model;
        
        public ResponseWithModel(String content, String model) {
            this.content = content;
            this.model = model;
        }
        
        public String getContent() {
            return content;
        }
        
        public String getModel() {
            return model;
        }
    }
}
