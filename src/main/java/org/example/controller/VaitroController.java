package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.VaiTroDto.VaiTroRequestDTO;
import org.example.dto.VaiTroDto.VaiTroResponseDTO;
import org.example.entity.VaiTro;
import org.example.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vai-tro")
@Tag(name = "VaiTro API", description = "APIs for managing roles (VaiTro)")
@CrossOrigin(origins = "*")
public class VaitroController {

    @Autowired
    private VaiTroService vaiTroService;

    @Operation(
            summary = "Tạo vai trò mới", 
            description = "Tạo một vai trò mới trong hệ thống. Các vai trò được phép: doc_gia, tac_gia, quan_tri, bien_tap"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", 
                    description = "Tạo vai trò thành công",
                    content = @Content(schema = @Schema(implementation = VaiTroResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Dữ liệu đầu vào không hợp lệ",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409", 
                    description = "Mã vai trò đã tồn tại",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody VaiTroRequestDTO request) {
        try {
            VaiTroResponseDTO created = vaiTroService.create(request);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Lấy danh sách tất cả vai trò", 
            description = "Lấy danh sách tất cả các vai trò trong hệ thống"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = VaiTroResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<VaiTroResponseDTO>> findAll() {
        return ResponseEntity.ok(vaiTroService.findAll());
    }

    @Operation(
            summary = "Lấy vai trò theo ID", 
            description = "Lấy thông tin chi tiết của một vai trò theo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Tìm thấy vai trò",
                    content = @Content(schema = @Schema(implementation = VaiTroResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Không tìm thấy vai trò",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
            @Parameter(description = "ID vai trò", example = "1", required = true)
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(vaiTroService.findById(id));
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Lấy vai trò theo mã", 
            description = "Lấy thông tin chi tiết của một vai trò theo mã (ma)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Tìm thấy vai trò",
                    content = @Content(schema = @Schema(implementation = VaiTroResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Không tìm thấy vai trò",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @GetMapping("/ma/{ma}")
    public ResponseEntity<?> findByMa(
            @Parameter(description = "Mã vai trò", example = "quan_tri", required = true)
            @PathVariable String ma) {
        try {
            return ResponseEntity.ok(vaiTroService.findByMa(ma));
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Cập nhật vai trò", 
            description = "Cập nhật thông tin của một vai trò đã tồn tại"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Cập nhật vai trò thành công",
                    content = @Content(schema = @Schema(implementation = VaiTroResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", 
                    description = "Dữ liệu đầu vào không hợp lệ",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Không tìm thấy vai trò",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID vai trò", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody VaiTroRequestDTO request) {
        try {
            return ResponseEntity.ok(vaiTroService.update(id, request));
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Khởi tạo vai trò mặc định", 
            description = "Tạo các vai trò mặc định nếu chưa tồn tại: doc_gia, tac_gia, quan_tri, bien_tap"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Các vai trò mặc định đã sẵn sàng",
                    content = @Content(schema = @Schema(implementation = VaiTroResponseDTO.class))
            )
    })
    @PostMapping("/init-defaults")
    public ResponseEntity<List<VaiTroResponseDTO>> initDefaults() {
        return ResponseEntity.ok(vaiTroService.initDefaultRoles());
    }

    @Operation(
            summary = "Xóa vai trò", 
            description = "Xóa một vai trò khỏi hệ thống theo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", 
                    description = "Xóa vai trò thành công",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "404", 
                    description = "Không tìm thấy vai trò",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "409", 
                    description = "Không thể xóa vai trò đang được sử dụng",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @Parameter(description = "ID vai trò", example = "1", required = true)
            @PathVariable Long id) {
        try {
            vaiTroService.delete(id);
            Map<String, String> res = new HashMap<>();
            res.put("message", "Xóa vai trò thành công");
            return ResponseEntity.ok(res);
        } catch (RuntimeException ex) {
            Map<String, String> res = new HashMap<>();
            res.put("error", ex.getMessage());
            return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
        }
    }
}


