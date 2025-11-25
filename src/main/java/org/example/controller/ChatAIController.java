package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.request.ai.AiChatRequest;
import org.example.dto.request.ai.GenerateCvRequest;
import org.example.dto.response.generatedCv.GeneratedCvPreviewResponse;
import org.example.dto.response.generatedCv.GeneratedCvResponse;
import org.example.service.ChatAIService;
import org.example.service.GeneratedCvService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller xử lý các API liên quan đến Chat AI và tạo CV
 */
@RestController
@RequestMapping("/api/ai/chat")
@Tag(name = "Trợ lý AI", description = "Các API liên quan đến Chat AI và tạo CV")
public class ChatAIController {

    private final ChatAIService chatAIService;
    private final GeneratedCvService generatedCvService;

    public ChatAIController(ChatAIService chatAIService, GeneratedCvService generatedCvService) {
        this.chatAIService = chatAIService;
        this.generatedCvService = generatedCvService;
    }

    // ==================== CHAT APIs ====================

    @Operation(summary = "Tìm kiếm template theo yêu cầu", 
               description = "Sử dụng AI để gợi ý các template phù hợp dựa trên mô tả của người dùng")
    @GetMapping("/search-template")
    public ResponseEntity<List<String>> searchTemplate(@RequestParam String query) {
        return ResponseEntity.ok(chatAIService.findTemplates(query));
    }

    @Operation(summary = "Chat với AI", description = "Trò chuyện tự do với AI về CV và xin việc")
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody AiChatRequest request) {
        String response = chatAIService.chat(request.getPrompt());
        return ResponseEntity.ok(Map.of("response", response));
    }
    
    // ==================== GENERATED CV APIs ====================
    
    @Operation(summary = "Preview CV từ AI", 
               description = "Tạo CV preview từ AI mà không lưu vào database. Trả về: status, title, dataJson (object), styleJson (object), htmlOutput")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tạo CV preview thành công",
                     content = @Content(schema = @Schema(implementation = GeneratedCvPreviewResponse.class))),
        @ApiResponse(responseCode = "400", description = "Prompt không hợp lệ")
    })
    @PostMapping("/cv/preview")
    public ResponseEntity<GeneratedCvPreviewResponse> previewCV(@RequestBody AiChatRequest request) {
        GeneratedCvPreviewResponse response = generatedCvService.generateCVPreview(request.getPrompt());
        if ("error".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Tạo và lưu CV từ AI", 
               description = "Tạo CV từ AI và lưu vào database. Yêu cầu userId và templateId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tạo CV thành công",
                     content = @Content(schema = @Schema(implementation = GeneratedCvResponse.class))),
        @ApiResponse(responseCode = "400", description = "Request không hợp lệ"),
        @ApiResponse(responseCode = "404", description = "User hoặc Template không tồn tại")
    })
    @PostMapping("/cv/generate")
    public ResponseEntity<?> generateAndSaveCV(@Valid @RequestBody GenerateCvRequest request) {
        try {
            GeneratedCvResponse response = generatedCvService.generateAndSaveCV(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @Operation(summary = "Lấy CV theo ID", description = "Trả về thông tin chi tiết của CV")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @GetMapping("/cv/{id}")
    public ResponseEntity<?> getCVById(
            @Parameter(description = "ID của CV") @PathVariable Long id) {
        try {
            GeneratedCvResponse response = generatedCvService.getCVById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @Operation(summary = "Lấy tất cả CV của user", 
               description = "Trả về danh sách tất cả CV đã tạo của một user")
    @GetMapping("/cv/user/{userId}")
    public ResponseEntity<List<GeneratedCvResponse>> getCVsByUserId(
            @Parameter(description = "ID của user") @PathVariable Long userId) {
        List<GeneratedCvResponse> responses = generatedCvService.getCVsByUserId(userId);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "Cập nhật CV", description = "Cập nhật thông tin CV đã tồn tại")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @PutMapping("/cv/{id}")
    public ResponseEntity<?> updateCV(
            @Parameter(description = "ID của CV") @PathVariable Long id,
            @RequestBody GeneratedCvResponse updateRequest) {
        try {
            GeneratedCvResponse response = generatedCvService.updateCV(id, updateRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @Operation(summary = "Xóa CV", description = "Xóa CV theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @DeleteMapping("/cv/{id}")
    public ResponseEntity<Void> deleteCV(
            @Parameter(description = "ID của CV") @PathVariable Long id) {
        try {
            generatedCvService.deleteCV(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Lưu CV từ preview", 
               description = "Lưu CV vào database sau khi user đã preview và chấp nhận")
    @PostMapping("/cv/save-preview")
    public ResponseEntity<?> savePreviewCV(
            @Parameter(description = "ID của user") @RequestParam Long userId,
            @Parameter(description = "ID của template") @RequestParam Long templateId,
            @RequestBody GeneratedCvResponse previewResponse) {
        try {
            GeneratedCvResponse response = generatedCvService.savePreviewCV(userId, templateId, previewResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
