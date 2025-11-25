package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.generatedCv.GeneratedCvCreateRequest;
import org.example.dto.response.generatedCv.GeneratedCvResponse;
import org.example.service.GeneratedCvManagementService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller quản lý CV đã tạo
 * Base path: /api/generated-cvs
 */
@RestController
@RequestMapping("/api/generated-cvs")
@RequiredArgsConstructor
@Tag(name = "Generated CV", description = "API quản lý CV đã tạo từ template")
public class GeneratedCvController {

    private final GeneratedCvManagementService generatedCvManagementService;

    // ==================== CREATE ====================

    @Operation(summary = "Tạo CV mới", 
               description = "Tạo CV từ template và dữ liệu user cung cấp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tạo CV thành công",
                     content = @Content(schema = @Schema(implementation = GeneratedCvResponse.class))),
        @ApiResponse(responseCode = "400", description = "Request không hợp lệ"),
        @ApiResponse(responseCode = "404", description = "User hoặc Template không tồn tại")
    })
    @PostMapping
    public ResponseEntity<?> createCV(@Valid @RequestBody GeneratedCvCreateRequest request) {
        GeneratedCvResponse response = generatedCvManagementService.createCV(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================== READ ====================

    @Operation(summary = "Lấy danh sách CV của user", 
               description = "Lấy danh sách CV đã tạo của user với phân trang")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công")
    })
    @GetMapping("/my-cvs")
    public ResponseEntity<?> getMyCVs(
            @Parameter(description = "ID của user") 
            @RequestParam Long userId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng mỗi trang") 
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Từ khóa tìm kiếm theo title") 
            @RequestParam(required = false) String keyword) {
        
        Page<GeneratedCvResponse> cvPage = generatedCvManagementService.getCVsByUserId(userId, page, size, keyword);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", cvPage.getContent());
        response.put("pagination", buildPaginationInfo(cvPage));
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy chi tiết CV", 
               description = "Lấy thông tin chi tiết CV bao gồm HTML và các dữ liệu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công",
                     content = @Content(schema = @Schema(implementation = GeneratedCvResponse.class))),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCVById(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id) {
        GeneratedCvResponse response = generatedCvManagementService.getCVById(id);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "success");
        result.put("data", response);
        
        return ResponseEntity.ok(result);
    }

    // ==================== UPDATE ====================

    @Operation(summary = "Cập nhật CV", 
               description = "Cập nhật thông tin CV đã tồn tại")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCV(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id,
            @Valid @RequestBody GeneratedCvCreateRequest request) {
        GeneratedCvResponse response = generatedCvManagementService.updateCV(id, request);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "success");
        result.put("message", "Cập nhật CV thành công");
        result.put("data", response);
        
        return ResponseEntity.ok(result);
    }

    // ==================== DELETE ====================

    @Operation(summary = "Xóa CV", 
               description = "Xóa CV đã tạo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCV(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id) {
        generatedCvManagementService.deleteCV(id);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Xóa CV thành công");
        
        return ResponseEntity.ok(response);
    }

    // ==================== HELPER ====================

    private Map<String, Object> buildPaginationInfo(Page<?> page) {
        Map<String, Object> pagination = new LinkedHashMap<>();
        pagination.put("currentPage", page.getNumber());
        pagination.put("totalPages", page.getTotalPages());
        pagination.put("totalElements", page.getTotalElements());
        pagination.put("size", page.getSize());
        pagination.put("hasNext", page.hasNext());
        pagination.put("hasPrevious", page.hasPrevious());
        return pagination;
    }
}
