package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.DanhMucDto.DanhMucCreateDto;
import org.example.dto.DanhMucDto.DanhMucResponseDto;
import org.example.dto.DanhMucDto.DanhMucUpdateDto;
import org.example.service.DanhMucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/danh-muc")
@Tag(name = "DanhMuc API", description = "APIs for managing categories (DanhMuc)")
@CrossOrigin(origins = "*")
public class DanhMucController {

    @Autowired
    private DanhMucService danhMucService;

    @Operation(
            summary = "Tạo danh mục mới",
            description = "Tạo một danh mục mới trong hệ thống. Tên và đường dẫn phải là duy nhất."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tạo danh mục thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Tên hoặc đường dẫn đã tồn tại")
    })
    @PostMapping
    public ResponseEntity<?> createDanhMuc(
            @Valid @RequestBody DanhMucCreateDto requestDTO) {
        try {
            DanhMucResponseDto responseDTO = danhMucService.createDanhMuc(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Lấy danh sách tất cả danh mục",
            description = "Lấy danh sách tất cả danh mục trong hệ thống, sắp xếp theo thứ tự"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<DanhMucResponseDto>> getAllDanhMuc() {
        List<DanhMucResponseDto> danhMucs = danhMucService.getAllDanhMuc();
        return new ResponseEntity<>(danhMucs, HttpStatus.OK);
    }

    @Operation(
            summary = "Lấy danh sách danh mục đang kích hoạt",
            description = "Lấy danh sách các danh mục đang được kích hoạt, sắp xếp theo thứ tự"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            )
    })
    @GetMapping("/active")
    public ResponseEntity<List<DanhMucResponseDto>> getAllActiveDanhMuc() {
        List<DanhMucResponseDto> danhMucs = danhMucService.getAllActiveDanhMuc();
        return new ResponseEntity<>(danhMucs, HttpStatus.OK);
    }

    @Operation(
            summary = "Lấy danh mục theo ID",
            description = "Lấy thông tin chi tiết của một danh mục theo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh mục thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDanhMucById(
            @Parameter(description = "ID danh mục", example = "1")
            @PathVariable Long id) {
        try {
            DanhMucResponseDto responseDTO = danhMucService.getDanhMucById(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Lấy danh mục theo đường dẫn",
            description = "Lấy thông tin chi tiết của một danh mục theo đường dẫn URL"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh mục thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @GetMapping("/duong-dan/{duongDan}")
    public ResponseEntity<?> getDanhMucByDuongDan(
            @Parameter(description = "Đường dẫn danh mục", example = "tin-tuc")
            @PathVariable String duongDan) {
        try {
            DanhMucResponseDto responseDTO = danhMucService.getDanhMucByDuongDan(duongDan);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Lấy danh mục theo tên",
            description = "Lấy thông tin chi tiết của một danh mục theo tên"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh mục thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @GetMapping("/ten/{ten}")
    public ResponseEntity<?> getDanhMucByTen(
            @Parameter(description = "Tên danh mục", example = "Tin tức")
            @PathVariable String ten) {
        try {
            DanhMucResponseDto responseDTO = danhMucService.getDanhMucByTen(ten);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Lấy danh mục theo danh mục cha",
            description = "Lấy danh sách các danh mục con theo danh mục cha"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            )
    })
    @GetMapping("/parent/{danhMucCha}")
    public ResponseEntity<List<DanhMucResponseDto>> getDanhMucByDanhMucCha(
            @Parameter(description = "ID danh mục cha", example = "1")
            @PathVariable Long danhMucCha) {
        List<DanhMucResponseDto> danhMucs = danhMucService.getDanhMucByDanhMucCha(danhMucCha);
        return new ResponseEntity<>(danhMucs, HttpStatus.OK);
    }

    @Operation(
            summary = "Lấy danh mục theo danh mục cha và trạng thái",
            description = "Lấy danh sách các danh mục con theo danh mục cha và trạng thái kích hoạt"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            )
    })
    @GetMapping("/parent/{danhMucCha}/status/{kichHoat}")
    public ResponseEntity<List<DanhMucResponseDto>> getDanhMucByDanhMucChaAndKichHoat(
            @Parameter(description = "ID danh mục cha", example = "1")
            @PathVariable Long danhMucCha,
            @Parameter(description = "Trạng thái kích hoạt", example = "true")
            @PathVariable Boolean kichHoat) {
        List<DanhMucResponseDto> danhMucs = danhMucService.getDanhMucByDanhMucChaAndKichHoat(danhMucCha, kichHoat);
        return new ResponseEntity<>(danhMucs, HttpStatus.OK);
    }

    @Operation(
            summary = "Lấy danh mục theo trạng thái kích hoạt",
            description = "Lấy danh sách các danh mục theo trạng thái kích hoạt"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            )
    })
    @GetMapping("/status/{kichHoat}")
    public ResponseEntity<List<DanhMucResponseDto>> getDanhMucByKichHoat(
            @Parameter(description = "Trạng thái kích hoạt", example = "true")
            @PathVariable Boolean kichHoat) {
        List<DanhMucResponseDto> danhMucs = danhMucService.getDanhMucByKichHoat(kichHoat);
        return new ResponseEntity<>(danhMucs, HttpStatus.OK);
    }

    @Operation(
            summary = "Tìm kiếm danh mục",
            description = "Tìm kiếm danh mục theo từ khóa trong tên hoặc mô tả"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tìm kiếm thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<DanhMucResponseDto>> searchDanhMuc(
            @Parameter(description = "Từ khóa tìm kiếm", example = "tin tức")
            @RequestParam String keyword) {
        List<DanhMucResponseDto> danhMucs = danhMucService.searchDanhMuc(keyword);
        return new ResponseEntity<>(danhMucs, HttpStatus.OK);
    }

    @Operation(
            summary = "Cập nhật danh mục",
            description = "Cập nhật thông tin của một danh mục theo ID. Chỉ các trường được cung cấp mới được cập nhật."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật danh mục thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Tên hoặc đường dẫn đã tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDanhMuc(
            @Parameter(description = "ID danh mục", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody DanhMucUpdateDto updateDTO) {
        try {
            DanhMucResponseDto responseDTO = danhMucService.updateDanhMuc(id, updateDTO);
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
            summary = "Chuyển đổi trạng thái kích hoạt",
            description = "Chuyển đổi trạng thái kích hoạt của danh mục (true/false)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Chuyển đổi trạng thái thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> toggleKichHoat(
            @Parameter(description = "ID danh mục", example = "1")
            @PathVariable Long id) {
        try {
            DanhMucResponseDto responseDTO = danhMucService.toggleKichHoat(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Cập nhật thứ tự",
            description = "Cập nhật thứ tự hiển thị của danh mục"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật thứ tự thành công",
                    content = @Content(schema = @Schema(implementation = DanhMucResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @PatchMapping("/{id}/order")
    public ResponseEntity<?> updateThuTu(
            @Parameter(description = "ID danh mục", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Thứ tự mới", example = "5")
            @RequestParam Integer thuTu) {
        try {
            DanhMucResponseDto responseDTO = danhMucService.updateThuTu(id, thuTu);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Xóa danh mục",
            description = "Xóa một danh mục khỏi hệ thống theo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa danh mục thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDanhMuc(
            @Parameter(description = "ID danh mục", example = "1")
            @PathVariable Long id) {
        try {
            danhMucService.deleteDanhMuc(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xóa danh mục thành công");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Không tìm thấy danh mục");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}