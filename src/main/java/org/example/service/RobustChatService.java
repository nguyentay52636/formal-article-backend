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
    
    private final List<String> models = List.of(
        "x-ai/grok-4.1-fast:free",
        "deepseek/deepseek-chat-v3-0324:free",
        "openai/gpt-5.1",
        "meta-llama/llama-3.3-70b-instruct:free",
        "meta-llama/llama-3.2-3b-instruct:free",
        "google/gemini-flash-1.5-8b:free",
        "qwen/qwen-2.5-7b-instruct:free"
    );

    private static final int DELAY_BETWEEN_RETRIES_MS = 500;

    public RobustChatService() {
        this.restTemplate = new RestTemplate();
    }

    public String askAI(String prompt) {
        for (String model : models) {
            try {
                System.out.println("Đang thử model: " + model);
                
                long startTime = System.currentTimeMillis();
                
                // Tạo request body
                List<OpenRouterRequest.Message> messages = new ArrayList<>();
                messages.add(new OpenRouterRequest.Message("user", prompt));
                
                OpenRouterRequest request = new OpenRouterRequest(model, messages, 0.7);
                
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
                
                System.out.println("Model " + model + " hoàn thành sau " + duration + "ms");

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    OpenRouterResponse responseBody = response.getBody();
                    
                    // Kiểm tra lỗi từ API
                    if (responseBody.getError() != null) {
                        System.out.println("API trả về lỗi: " + responseBody.getError().getMessage());
                        continue;
                    }
                    
                    String output = responseBody.getContent();
                    
                    if (output != null && !output.isEmpty() && !output.trim().isEmpty()) {
                        System.out.println("Thành công với model: " + model);
                        return output;
                    }
                }

            } catch (Exception e) {
                System.out.println("Model " + model + " thất bại: " + e.getMessage());
                e.printStackTrace();
                
                // Đợi một chút trước khi thử model tiếp theo
                try {
                    Thread.sleep(DELAY_BETWEEN_RETRIES_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return "Hiện tại hệ thống AI đang bận, vui lòng thử lại sau.";
                }
            }
        }
        return "Hiện tại hệ thống AI đang bận, vui lòng thử lại sau.";
    }
}
