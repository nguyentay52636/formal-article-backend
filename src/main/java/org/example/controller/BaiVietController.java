package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.BaiVietDto.BaiVietCreateDto;
import org.example.dto.BaiVietDto.BaiVietResponseDto;
import org.example.dto.BaiVietDto.BaiVietUpdateDto;
import org.example.entity.BaiViet;
import org.example.service.BaiVietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bai-viet")
@Tag(name = "Bai Viet API", description = "APIs for managing articles (BaiViet)")
@CrossOrigin(origins = "*")
public class BaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    @Operation(
            summary = "Thêm mới",
            description = "Tạo bài viết mới"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Article created successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "URL path already exists")
    })
    @PostMapping
    public ResponseEntity<?> createBaiViet(
            @Valid @RequestBody BaiVietCreateDto createDto,
            @Parameter(description = "Author ID", example = "1")
            @RequestParam Long tacGiaId) {
        try {
            BaiVietResponseDto responseDto = baiVietService.createBaiViet(createDto, tacGiaId);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Lấy tất cả bài viết",
            description = "Retrieves a list of all articles in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of articles",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllBaiViet() {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getAllBaiViet();
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Chi tiết bài viết",
            description = "Lấy chi tiết 1 bài viết theo id hoặc slug"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Article found",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBaiVietByIdOrSlug(
            @Parameter(description = "Article ID hoặc slug", example = "1 hoặc bai-viet-mau")
            @PathVariable String id) {
        try {
            BaiVietResponseDto responseDto;
            
            // Kiểm tra xem là ID (số) hay slug (chuỗi)
            if (id.matches("\\d+")) {
                // Là ID
                responseDto = baiVietService.getBaiVietById(Long.parseLong(id));
            } else {
                // Là slug
                responseDto = baiVietService.getBaiVietByDuongDan(id);
            }
            
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Bài viết nổi bật",
            description = "Lấy danh sách các bài viết nổi bật (join với noi_bat_bai_viet)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Featured articles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/noi-bat")
    public ResponseEntity<?> getNoiBatBaiViet() {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getNoiBatBaiViet();
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get published articles",
            description = "Retrieves a list of published articles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Published articles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/published")
    public ResponseEntity<?> getPublishedBaiViet() {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getPublishedArticles();
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Tìm kiếm bài viết",
            description = "Tìm theo tiêu đề, nội dung, mô tả, tác giả, v.v."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/tim-kiem")
    public ResponseEntity<?> timKiemBaiViet(
            @Parameter(description = "Từ khóa tìm kiếm", example = "java")
            @RequestParam String keyword) {
        try {
            List<BaiVietResponseDto> articles = baiVietService.searchByKeyword(keyword);
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get articles by category",
            description = "Retrieves articles filtered by category ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Articles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/category/{danhMucId}")
    public ResponseEntity<?> getBaiVietByDanhMuc(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable Long danhMucId) {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getBaiVietByDanhMuc(danhMucId);
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get articles by author",
            description = "Retrieves articles filtered by author ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Articles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/author/{tacGiaId}")
    public ResponseEntity<?> getBaiVietByTacGia(
            @Parameter(description = "Author ID", example = "1")
            @PathVariable Long tacGiaId) {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getBaiVietByTacGia(tacGiaId);
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get articles by tag",
            description = "Retrieves published articles filtered by tag ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Articles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/tag/{theId}")
    public ResponseEntity<?> getBaiVietByThe(
            @Parameter(description = "Tag ID", example = "1")
            @PathVariable Long theId) {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getBaiVietByThe(theId);
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get popular articles",
            description = "Retrieves the most popular published articles based on reactions"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Popular articles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularArticles() {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getPopularArticles();
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Cập nhật",
            description = "Cập nhật nội dung, tiêu đề, hình ảnh, v.v."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Article updated successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Article not found"),
            @ApiResponse(responseCode = "409", description = "URL path already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBaiViet(
            @Parameter(description = "Article ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BaiVietUpdateDto updateDto) {
        try {
            BaiVietResponseDto responseDto = baiVietService.updateBaiViet(id, updateDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            if (e.getMessage().contains("không tồn tại")) {
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            } else if (e.getMessage().contains("đã tồn tại")) {
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Đăng / Gỡ bài",
            description = "Thay đổi trạng thái: \"nháp → đã đăng\" hoặc \"đã đăng → ẩn\""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Article status updated successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<?> thayDoiTrangThai(
            @Parameter(description = "Article ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Trạng thái mới", example = "XUAT_BAN")
            @RequestParam BaiViet.TrangThai trangThai) {
        try {
            BaiVietResponseDto responseDto = baiVietService.thayDoiTrangThai(id, trangThai);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Xóa",
            description = "Xóa (hoặc đánh dấu đã xóa) bài viết"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBaiViet(
            @Parameter(description = "Article ID", example = "1")
            @PathVariable Long id) {
        try {
            baiVietService.deleteBaiViet(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Article deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Article not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get article statistics",
            description = "Retrieves statistics about articles"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    @GetMapping("/statistics")
    public ResponseEntity<?> getArticleStatistics() {
        try {
            Map<String, Object> statistics = baiVietService.getArticleStatistics();
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
