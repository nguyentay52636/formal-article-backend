package org.example.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.request.ai.AiImageGenerateRequest;
import org.example.service.ImageAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/image")
@Tag(name = "Image AI", description = "API tạo ảnh bằng AI")
public class ImageAIController {

    private final ImageAIService imageService;

    public ImageAIController(ImageAIService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "Tạo ảnh từ văn bản", description = "Tạo ảnh dựa trên prompt, có tự động tối ưu và fallback model")
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateImage(@RequestBody AiImageGenerateRequest request) {
        String imageUrl = imageService.generateImage(request.getPrompt());
        return ResponseEntity.ok(Map.of("url", imageUrl));
    }
}
