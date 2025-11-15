package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.request.favouriteCv.FavouriteCvCreateRequest;
import org.example.dto.request.favouriteCv.FavouriteCvUpdateRequest;
import org.example.dto.response.favouriteCv.FavouriteCvResponse;
import org.example.service.FavouriteCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourite-cvs")
@Tag(name = "Yêu thích CV (Favourite CV)", description = "API quản lý danh sách yêu thích CV trong hệ thống")
public class FavouriteCvController {
    
    @Autowired
    private FavouriteCvService favouriteCvService;
    
    /**
     * Lấy FavouriteCv theo ID
     * GET /api/favourite-cvs/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy yêu thích CV theo ID", description = "Trả về thông tin chi tiết của yêu thích CV dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tìm thấy yêu thích CV"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy yêu thích CV với ID đã cho")
    })
    public ResponseEntity<FavouriteCvResponse> getFavouriteCvById(
            @Parameter(description = "ID của yêu thích CV cần lấy", required = true)
            @PathVariable Long id) {
        FavouriteCvResponse favouriteCv = favouriteCvService.getFavouriteCvById(id);
        if (favouriteCv != null) {
            return ResponseEntity.ok(favouriteCv);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy tất cả FavouriteCv
     * GET /api/favourite-cvs
     */
    @GetMapping
    @Operation(summary = "Lấy tất cả yêu thích CV", description = "Trả về danh sách tất cả các yêu thích CV trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Danh sách yêu thích CV")
    public ResponseEntity<List<FavouriteCvResponse>> getAllFavouriteCv() {
        List<FavouriteCvResponse> favouriteCvs = favouriteCvService.getAllFavouriteCv();
        return ResponseEntity.ok(favouriteCvs);
    }
    
    /**
     * Tạo FavouriteCv mới (thêm CV vào danh sách yêu thích)
     * POST /api/favourite-cvs
     */
    @PostMapping
    @Operation(summary = "Thêm CV vào danh sách yêu thích", description = "Thêm một CV vào danh sách yêu thích của user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "CV đã được thêm vào danh sách yêu thích thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc CV đã có trong danh sách yêu thích")
    })
    public ResponseEntity<?> createFavouriteCv(
            @Parameter(description = "Thông tin yêu thích CV cần tạo", required = true)
            @Valid @RequestBody FavouriteCvCreateRequest request) {
        try {
            FavouriteCvResponse favouriteCv = favouriteCvService.createFavouriteCv(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(favouriteCv);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * Cập nhật FavouriteCv
     * PUT /api/favourite-cvs/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật yêu thích CV", description = "Cập nhật thông tin của yêu thích CV dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Yêu thích CV đã được cập nhật thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc CV đã có trong danh sách yêu thích"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy yêu thích CV với ID đã cho")
    })
    public ResponseEntity<?> updateFavouriteCv(
            @Parameter(description = "ID của yêu thích CV cần cập nhật", required = true)
            @PathVariable Long id,
            @Parameter(description = "Thông tin yêu thích CV cần cập nhật", required = true)
            @Valid @RequestBody FavouriteCvUpdateRequest request) {
        try {
            FavouriteCvResponse favouriteCv = favouriteCvService.updateFavouriteCv(id, request);
            return ResponseEntity.ok(favouriteCv);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("không tồn tại")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * Xóa FavouriteCv (xóa CV khỏi danh sách yêu thích)
     * DELETE /api/favourite-cvs/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa CV khỏi danh sách yêu thích", description = "Xóa CV khỏi danh sách yêu thích dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "CV đã được xóa khỏi danh sách yêu thích thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy yêu thích CV với ID đã cho")
    })
    public ResponseEntity<?> deleteFavouriteCv(
            @Parameter(description = "ID của yêu thích CV cần xóa", required = true)
            @PathVariable Long id) {
        try {
            favouriteCvService.deleteFavouriteCv(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    /**
     * Lấy tất cả FavouriteCv theo User ID
     * GET /api/favourite-cvs/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Lấy danh sách yêu thích CV theo User ID", description = "Trả về danh sách tất cả CV yêu thích của một user")
    @ApiResponse(responseCode = "200", description = "Danh sách yêu thích CV của user")
    public ResponseEntity<List<FavouriteCvResponse>> getFavouriteCvByUserId(
            @Parameter(description = "ID của user", required = true)
            @PathVariable Long userId) {
        List<FavouriteCvResponse> favouriteCvs = favouriteCvService.getFavouriteCvByUserId(userId);
        return ResponseEntity.ok(favouriteCvs);
    }
    
    /**
     * Lấy tất cả FavouriteCv theo Template ID
     * GET /api/favourite-cvs/template/{templateId}
     */
    @GetMapping("/template/{templateId}")
    @Operation(summary = "Lấy danh sách user yêu thích CV theo Template ID", description = "Trả về danh sách tất cả user đã yêu thích một template CV")
    @ApiResponse(responseCode = "200", description = "Danh sách user yêu thích template")
    public ResponseEntity<List<FavouriteCvResponse>> getFavouriteCvByTemplateId(
            @Parameter(description = "ID của template", required = true)
            @PathVariable Long templateId) {
        List<FavouriteCvResponse> favouriteCvs = favouriteCvService.getFavouriteCvByTemplateId(templateId);
        return ResponseEntity.ok(favouriteCvs);
    }
}

