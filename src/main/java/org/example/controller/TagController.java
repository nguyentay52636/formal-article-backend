package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.tag.TagCreateRequest;
import org.example.dto.request.tag.TagUpdateRequest;
import org.example.dto.response.tag.TagResponse;
import org.example.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tag", description = "API quản lý danh mục CV (Tag)")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    @Operation(summary = "Lấy danh sách tag", description = "Lấy tất cả các tag (danh mục CV)")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết tag", description = "Lấy thông tin chi tiết của một tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "404", description = "Tag không tồn tại")
    })
    public ResponseEntity<TagResponse> getTagById(
            @Parameter(description = "ID của tag", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PostMapping
    @Operation(summary = "Tạo tag mới", description = "Tạo một tag mới")
    @ApiResponse(responseCode = "200", description = "Tạo thành công")
    public ResponseEntity<TagResponse> createTag(@RequestBody TagCreateRequest request) {
        return ResponseEntity.ok(tagService.createTag(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật tag", description = "Cập nhật thông tin tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Tag không tồn tại")
    })
    public ResponseEntity<TagResponse> updateTag(
            @Parameter(description = "ID của tag", required = true) @PathVariable Long id,
            @RequestBody TagUpdateRequest request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa tag", description = "Xóa một tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Tag không tồn tại")
    })
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "ID của tag", required = true) @PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }
}
