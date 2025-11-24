package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.comment.CommentRequest;
import org.example.dto.request.rating.RatingRequest;
import org.example.dto.request.template.TemplateCreateRequest;
import org.example.dto.request.template.TemplateUpdateRequest;
import org.example.dto.response.comment.CommentResponse;
import org.example.dto.response.rating.RatingResponse;
import org.example.dto.response.template.TemplateResponse;
import org.example.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Template", description = "API quản lý Template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping("/all")
    @Operation(summary = "Lấy tất cả template", description = "Lấy danh sách tất cả template không phân trang, không lọc")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<TemplateResponse>> getAllTemplatesList() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    @GetMapping
    @Operation(summary = "Lọc danh sách template", description = "Lấy danh sách template có thể lọc theo language, design, usage")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<TemplateResponse>> filterTemplates(
            @Parameter(description = "Lọc theo ngôn ngữ") @RequestParam(required = false) String language,
            @Parameter(description = "Lọc theo thiết kế") @RequestParam(required = false) String design,
            @Parameter(description = "Lọc theo lĩnh vực sử dụng") @RequestParam(required = false) String usage) {
        return ResponseEntity.ok(templateService.filterTemplates(language, design, usage));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết template", description = "Lấy thông tin chi tiết của một template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Template không tồn tại")
    })
    public ResponseEntity<TemplateResponse> getTemplateById(
            @Parameter(description = "ID của template", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(templateService.getTemplateById(id));
    }

    @PostMapping
    @Operation(summary = "Tạo template mới", description = "Tạo một template mới")
    @ApiResponse(responseCode = "200", description = "Tạo thành công")
    public ResponseEntity<TemplateResponse> createTemplate(@RequestBody TemplateCreateRequest request) {
        return ResponseEntity.ok(templateService.createTemplate(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật template", description = "Cập nhật thông tin template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Template không tồn tại")
    })
    public ResponseEntity<TemplateResponse> updateTemplate(
            @Parameter(description = "ID của template", required = true) @PathVariable Long id,
            @RequestBody TemplateUpdateRequest request) {
        return ResponseEntity.ok(templateService.updateTemplate(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa template", description = "Xóa một template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Template không tồn tại")
    })
    public ResponseEntity<Void> deleteTemplate(
            @Parameter(description = "ID của template", required = true) @PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/views")
    @Operation(summary = "Tăng lượt xem", description = "Tăng số lượt xem cho template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Template không tồn tại")
    })
    public ResponseEntity<Void> increaseViews(
            @Parameter(description = "ID của template", required = true) @PathVariable Long id) {
        templateService.increaseViews(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tag/{tagId}")
    @Operation(summary = "Lấy template theo tag", description = "Lấy danh sách template thuộc một tag")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<TemplateResponse>> getTemplatesByTag(
            @Parameter(description = "ID của tag", required = true) @PathVariable Long tagId) {
        return ResponseEntity.ok(templateService.getTemplatesByTag(tagId));
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Comment template", description = "Thêm bình luận cho template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Template hoặc User không tồn tại")
    })
    public ResponseEntity<CommentResponse> addComment(
            @Parameter(description = "ID của template", required = true) @PathVariable Long id,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(templateService.addComment(id, request));
    }

    @PostMapping("/{id}/ratings")
    @Operation(summary = "Rating template", description = "Đánh giá template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Template hoặc User không tồn tại")
    })
    public ResponseEntity<RatingResponse> addRating(
            @Parameter(description = "ID của template", required = true) @PathVariable Long id,
            @RequestBody RatingRequest request) {
        return ResponseEntity.ok(templateService.addRating(id, request));
    }
}