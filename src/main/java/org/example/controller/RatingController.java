package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.rating.RatingRequest;
import org.example.dto.request.rating.RatingUpdateRequest;
import org.example.dto.response.rating.RatingResponse;
import org.example.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@Tag(name = "Rating", description = "API quản lý đánh giá")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    @Operation(summary = "Tạo đánh giá", description = "Tạo một đánh giá mới cho template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "User đã đánh giá template này hoặc dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Template hoặc User không tồn tại")
    })
    public ResponseEntity<RatingResponse> createRating(@RequestBody RatingRequest request) {
        return ResponseEntity.ok(ratingService.createRating(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật đánh giá", description = "Cập nhật thông tin đánh giá (điểm số, user, template)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Rating không tồn tại")
    })
    public ResponseEntity<RatingResponse> updateRating(
            @Parameter(description = "ID của rating", required = true) @PathVariable Long id,
            @RequestBody RatingUpdateRequest request) {
        return ResponseEntity.ok(ratingService.updateRating(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa đánh giá", description = "Xóa một đánh giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Rating không tồn tại")
    })
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "ID của rating", required = true) @PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đánh giá", description = "Lấy thông tin chi tiết của một đánh giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Rating không tồn tại")
    })
    public ResponseEntity<RatingResponse> getRatingById(
            @Parameter(description = "ID của rating", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lấy đánh giá theo user", description = "Lấy danh sách đánh giá của một user")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<java.util.List<RatingResponse>> getRatingsByUserId(
            @Parameter(description = "ID của user", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
    }

    @GetMapping("/template/{templateId}")
    @Operation(summary = "Lấy đánh giá theo template", description = "Lấy danh sách đánh giá của một template")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<java.util.List<RatingResponse>> getRatingsByTemplateId(
            @Parameter(description = "ID của template", required = true) @PathVariable Long templateId) {
        return ResponseEntity.ok(ratingService.getRatingsByTemplateId(templateId));
    }
}
