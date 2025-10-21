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
            summary = "Create a new article",
            description = "Creates a new article with the provided information. URL path must be unique."
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
            summary = "Get all articles",
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
            summary = "Get article by ID",
            description = "Retrieves a specific article by its ID"
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
    public ResponseEntity<?> getBaiVietById(
            @Parameter(description = "Article ID", example = "1")
            @PathVariable Long id) {
        try {
            BaiVietResponseDto responseDto = baiVietService.getBaiVietById(id);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get article by URL path",
            description = "Retrieves a specific article by its URL path"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Article found",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/path/{duongDan}")
    public ResponseEntity<?> getBaiVietByDuongDan(
            @Parameter(description = "URL path", example = "bai-viet-mau")
            @PathVariable String duongDan) {
        try {
            BaiVietResponseDto responseDto = baiVietService.getBaiVietByDuongDan(duongDan);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
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
            summary = "Search articles by keyword",
            description = "Search published articles by keyword in title or summary"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchByKeyword(
            @Parameter(description = "Search keyword", example = "java")
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
            summary = "Get latest articles",
            description = "Retrieves the latest published articles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Latest articles retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            )
    })
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestArticles() {
        try {
            List<BaiVietResponseDto> articles = baiVietService.getLatestArticles();
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
            summary = "Update article",
            description = "Updates an existing article with new information"
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
            summary = "Publish article",
            description = "Publishes an article by changing its status to XUAT_BAN"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Article published successfully",
                    content = @Content(schema = @Schema(implementation = BaiVietResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publishBaiViet(
            @Parameter(description = "Article ID", example = "1")
            @PathVariable Long id) {
        try {
            BaiVietResponseDto responseDto = baiVietService.publishBaiViet(id);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Delete article",
            description = "Deletes an article by its ID"
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
