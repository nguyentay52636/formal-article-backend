package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.TepTin.TepTinResponseDto;
import org.example.dto.TepTin.TepTinUpdateDto;
import org.example.service.TepTinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tep-tin")
@Tag(name = "TepTin API", description = "APIs Quản lý tập tin (TepTin)")
@CrossOrigin(origins = "*")
public class TepTinController {
    
    @Autowired
    private TepTinService tepTinService;
    
    // ========== FILE UPLOAD & DOWNLOAD ==========
    
    @Operation(
            summary = "Upload file",
            description = "Tải lên file mới vào hệ thống"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Upload file thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "500", description = "Lỗi server khi upload file")
    })
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @Parameter(description = "File to upload")
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Mô tả file")
            @RequestParam(value = "moTa", required = false) String moTa,
            @Parameter(description = "Loại file (anh, tai_lieu, khac)")
            @RequestParam(value = "loaiTep", required = false) String loaiTep,
            @Parameter(description = "ID người tạo file")
            @RequestParam("nguoiTaoId") Long nguoiTaoId) {
        try {
            TepTinResponseDto responseDTO = tepTinService.uploadFile(file, moTa, loaiTep, nguoiTaoId);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(
            summary = "Increment download count",
            description = "Tăng số lượt tải xuống của file"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tăng lượt tải thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy file")
    })
    @PostMapping("/{id}/increment-download")
    public ResponseEntity<?> incrementDownloadCount(
            @Parameter(description = "ID file", example = "1")
            @PathVariable Long id) {
        try {
            TepTinResponseDto responseDTO = tepTinService.incrementDownloadCount(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    
    @Operation(
            summary = "Get all files",
            description = "Lấy danh sách tất cả file"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách file thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<TepTinResponseDto>> getAllFiles() {
        List<TepTinResponseDto> files = tepTinService.getAllFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    
    @Operation(
            summary = "Get file details",
            description = "Lấy thông tin chi tiết của file"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy thông tin file thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy file")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getFileById(
            @Parameter(description = "ID file", example = "1")
            @PathVariable Long id) {
        try {
            TepTinResponseDto responseDTO = tepTinService.getFileById(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(
            summary = "Update file info",
            description = "Cập nhật thông tin metadata của file"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật file thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy file"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Tên hoặc đường dẫn file đã tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFile(
            @Parameter(description = "ID file", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TepTinUpdateDto updateDTO) {
        try {
            TepTinResponseDto responseDTO = tepTinService.updateFile(id, updateDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            if (e.getMessage().contains("Không tìm thấy")) {
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            } else if (e.getMessage().contains("đã tồn tại")) {
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(
            summary = "Delete file",
            description = "Xóa file khỏi hệ thống"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa file thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy file")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFile(
            @Parameter(description = "ID file", example = "1")
            @PathVariable Long id) {
        try {
            tepTinService.deleteFile(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xóa file thành công");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Không tìm thấy file");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // ========== FILE SEARCH & FILTER ==========
    
    @Operation(
            summary = "Search files",
            description = "Tìm kiếm file theo từ khóa"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tìm kiếm file thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<TepTinResponseDto>> searchFiles(
            @Parameter(description = "Từ khóa tìm kiếm", example = "document")
            @RequestParam("keyword") String keyword,
            @Parameter(description = "Lọc theo loại file", example = "anh")
            @RequestParam(value = "loaiTep", required = false) String loaiTep) {
        List<TepTinResponseDto> files = tepTinService.searchFiles(keyword, loaiTep);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    
    @Operation(
            summary = "Get files by user",
            description = "Lấy tất cả file được tải lên bởi người dùng cụ thể"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy file theo người dùng thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            )
    })
    @GetMapping("/by-user/{nguoiTaoId}")
    public ResponseEntity<List<TepTinResponseDto>> getFilesByUser(
            @Parameter(description = "ID người dùng", example = "1")
            @PathVariable Long nguoiTaoId) {
        List<TepTinResponseDto> files = tepTinService.getFilesByUser(nguoiTaoId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    
    @Operation(
            summary = "Get files by type",
            description = "Lấy file được lọc theo loại"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy file theo loại thành công",
                    content = @Content(schema = @Schema(implementation = TepTinResponseDto.class))
            )
    })
    @GetMapping("/by-type/{loaiTep}")
    public ResponseEntity<List<TepTinResponseDto>> getFilesByType(
            @Parameter(description = "Loại file", example = "anh")
            @PathVariable String loaiTep) {
        List<TepTinResponseDto> files = tepTinService.getFilesByType(loaiTep);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}