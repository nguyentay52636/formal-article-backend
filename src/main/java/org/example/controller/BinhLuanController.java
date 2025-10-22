package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.BinhLuanDto.BinhLuanCreateDto;
import org.example.dto.BinhLuanDto.BinhLuanResponseDto;
import org.example.dto.BinhLuanDto.BinhLuanUpdateDto;
import org.example.service.BinhLuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/binh-luan")
@Tag(name = "Binh Luan API", description = "APIs for managing comments (BinhLuan)")
@CrossOrigin(origins = "*")
public class BinhLuanController {

    @Autowired
    private BinhLuanService binhLuanService;

    @Operation(
            summary = "Create a new comment",
            description = "Creates a new comment for an article. Requires a registered user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comment created successfully",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Article or user not found")
    })
    @PostMapping
    public ResponseEntity<?> createBinhLuan(@Valid @RequestBody BinhLuanCreateDto createDto) {
        try {
            BinhLuanResponseDto responseDto = binhLuanService.createBinhLuan(createDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get all comments",
            description = "Retrieves a list of all comments in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of comments",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<BinhLuanResponseDto>> getAllBinhLuan() {
        List<BinhLuanResponseDto> comments = binhLuanService.getAllBinhLuan();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(
            summary = "Get comment by ID",
            description = "Retrieves a specific comment by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment found",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBinhLuanById(
            @Parameter(description = "Comment ID", example = "1")
            @PathVariable Long id) {
        try {
            BinhLuanResponseDto responseDto = binhLuanService.getBinhLuanById(id);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get list of comments by article ID",
            description = "Retrieves all comments for a specific article"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            )
    })
    @GetMapping("/getListBinhLuan/{baiVietId}")
    public ResponseEntity<List<BinhLuanResponseDto>> getListBinhLuan(
            @Parameter(description = "Article ID", example = "1")
            @PathVariable Long baiVietId) {
        List<BinhLuanResponseDto> comments = binhLuanService.getBinhLuanByBaiVietId(baiVietId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(
            summary = "Get comments by user ID",
            description = "Retrieves all comments made by a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User comments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            )
    })
    @GetMapping("/user/{nguoiDungId}")
    public ResponseEntity<List<BinhLuanResponseDto>> getBinhLuanByNguoiDungId(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long nguoiDungId) {
        List<BinhLuanResponseDto> comments = binhLuanService.getBinhLuanByNguoiDungId(nguoiDungId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(
            summary = "Search comments by content",
            description = "Search comments by keyword in comment content"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<BinhLuanResponseDto>> searchBinhLuanByContent(
            @Parameter(description = "Search keyword", example = "great")
            @RequestParam String keyword) {
        List<BinhLuanResponseDto> comments = binhLuanService.searchBinhLuanByContent(keyword);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(
            summary = "Get comments by status",
            description = "Retrieves comments filtered by their status"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            )
    })
    @GetMapping("/status/{trangThai}")
    public ResponseEntity<List<BinhLuanResponseDto>> getBinhLuanByTrangThai(
            @Parameter(description = "Comment status", example = "active")
            @PathVariable String trangThai) {
        List<BinhLuanResponseDto> comments = binhLuanService.getBinhLuanByTrangThai(trangThai);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(
            summary = "Update comment",
            description = "Updates an existing comment's information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment updated successfully",
                    content = @Content(schema = @Schema(implementation = BinhLuanResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBinhLuan(
            @Parameter(description = "Comment ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BinhLuanUpdateDto updateDto) {
        try {
            BinhLuanResponseDto responseDto = binhLuanService.updateBinhLuan(id, updateDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Delete comment",
            description = "Deletes a comment from the system by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBinhLuan(
            @Parameter(description = "Comment ID", example = "1")
            @PathVariable Long id) {
        try {
            binhLuanService.deleteBinhLuan(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get comment statistics",
            description = "Retrieves statistics about comments"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    @GetMapping("/statistics")
    public ResponseEntity<?> getCommentStatistics(
            @Parameter(description = "Article ID (optional)", example = "1")
            @RequestParam(required = false) Long baiVietId,
            @Parameter(description = "User ID (optional)", example = "1")
            @RequestParam(required = false) Long nguoiDungId) {
        Map<String, Object> statistics = new HashMap<>();
        
        if (baiVietId != null) {
            statistics.put("articleCommentCount", binhLuanService.countBinhLuanByBaiVietId(baiVietId));
        }
        
        if (nguoiDungId != null) {
            statistics.put("userCommentCount", binhLuanService.countBinhLuanByNguoiDungId(nguoiDungId));
        }
        
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
