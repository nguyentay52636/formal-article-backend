package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.ChatAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/chat")
@Tag(name = "Trợ lý AI", description = "Các API liên quan đến Chat AI và gợi ý template")
public class ChatAIController {

    private final ChatAIService chatAIService;

    public ChatAIController(ChatAIService chatAIService) {
        this.chatAIService = chatAIService;
    }

    @Operation(summary = "Tìm kiếm template theo yêu cầu", description = "Sử dụng AI để gợi ý các template phù hợp dựa trên mô tả của người dùng")
    @GetMapping("/search-template")
    public ResponseEntity<List<String>> searchTemplate(@RequestParam String query) {
        return ResponseEntity.ok(chatAIService.findTemplates(query));
    }

    @Operation(summary = "Tạo CV HTML", description = "Tạo mã HTML/CSS cho CV dựa trên thông tin người dùng cung cấp")
    @PostMapping("/generate-cv")
    public ResponseEntity<Map<String, String>> generateCV(@RequestBody org.example.dto.request.ai.AiCvGenerateRequest request) {
        String html = chatAIService.generateCVHtml(
                request.getName(),
                request.getPosition(),
                request.getExperience(),
                request.getProjects()
        );
        return ResponseEntity.ok(Map.of("html", html));
    }
    @Operation(summary = "Chat với AI", description = "Trò chuyện tự do với AI")
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody org.example.dto.request.ai.AiChatRequest request) {
        String response = chatAIService.chat(request.getPrompt());
        return ResponseEntity.ok(Map.of("response", response));
    }
}
