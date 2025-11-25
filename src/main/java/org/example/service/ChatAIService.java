package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ChatAIService {

    private final RobustChatService robustChatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatAIService(RobustChatService robustChatService) {
        this.robustChatService = robustChatService;
    }

    public List<String> findTemplates(String userQuery) {
        String prompt = """
            Bạn là hệ thống gợi ý template CV. Người dùng hỏi:
            %s
            Trả về danh sách tên template phù hợp, dạng JSON array.
            """.formatted(userQuery);

        String aiResponse = robustChatService.askAI(prompt);

        try {
            // Clean up response if it contains markdown code blocks
            if (aiResponse.contains("```json")) {
                aiResponse = aiResponse.substring(aiResponse.indexOf("```json") + 7);
                if (aiResponse.contains("```")) {
                    aiResponse = aiResponse.substring(0, aiResponse.indexOf("```"));
                }
            } else if (aiResponse.contains("```")) {
                 aiResponse = aiResponse.substring(aiResponse.indexOf("```") + 3);
                 if (aiResponse.contains("```")) {
                    aiResponse = aiResponse.substring(0, aiResponse.indexOf("```"));
                }
            }
            
            return objectMapper.readValue(aiResponse.trim(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            System.err.println("Error parsing AI response: " + aiResponse);
            return Collections.emptyList();
        }
    }

    public String generateCVHtml(String name, String position, String experience, String projects) {
        String prompt = """
            Bạn là hệ thống tạo CV chuyên nghiệp.
            Tạo CV HTML + CSS hoàn chỉnh dựa trên thông tin:
            Name: %s
            Position: %s
            Experience: %s
            Projects: %s
            HTML phải responsive, đẹp mắt, có section rõ ràng.
            """.formatted(name, position, experience, projects);

        return robustChatService.askAI(prompt);
    }

    public String chat(String prompt) {
        return robustChatService.askAI(prompt);
    }
}
