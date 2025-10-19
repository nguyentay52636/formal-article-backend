package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.NguoiDungDto.NguoiDungRequestDTO;
import org.example.dto.NguoiDungDto.NguoiDungResponseDTO;
import org.example.dto.NguoiDungDto.NguoiDungUpdateDTO;
import org.example.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "APIs for managing users (NguoiDung)")
@CrossOrigin(origins = "*")
public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user with the provided information. Username and email must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping
    public ResponseEntity<?> createNguoiDung(
            @Valid @RequestBody NguoiDungRequestDTO requestDTO) {
        try {
            NguoiDungResponseDTO responseDTO = nguoiDungService.createNguoiDung(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of users",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<NguoiDungResponseDTO>> getAllNguoiDung() {
        List<NguoiDungResponseDTO> users = nguoiDungService.getAllNguoiDung();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getNguoiDungById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        try {
            NguoiDungResponseDTO responseDTO = nguoiDungService.getNguoiDungById(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get user by username",
            description = "Retrieves a specific user by their username"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{tenDangNhap}")
    public ResponseEntity<?> getNguoiDungByUsername(
            @Parameter(description = "Username", example = "johndoe123")
            @PathVariable String tenDangNhap) {
        try {
            NguoiDungResponseDTO responseDTO = nguoiDungService.getNguoiDungByUsername(tenDangNhap);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Get user by email",
            description = "Retrieves a specific user by their email address"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getNguoiDungByEmail(
            @Parameter(description = "Email address", example = "john.doe@example.com")
            @PathVariable String email) {
        try {
            NguoiDungResponseDTO responseDTO = nguoiDungService.getNguoiDungByEmail(email);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Search users by keyword",
            description = "Search users by keyword in username, email, or full name"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<NguoiDungResponseDTO>> searchNguoiDung(
            @Parameter(description = "Search keyword", example = "john")
            @RequestParam String keyword) {
        List<NguoiDungResponseDTO> users = nguoiDungService.searchNguoiDung(keyword);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Get users by status",
            description = "Retrieves users filtered by their status"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            )
    })
    @GetMapping("/status/{trangThai}")
    public ResponseEntity<List<NguoiDungResponseDTO>> getNguoiDungByStatus(
            @Parameter(description = "User status", example = "active")
            @PathVariable String trangThai) {
        List<NguoiDungResponseDTO> users = nguoiDungService.getNguoiDungByStatus(trangThai);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(
            summary = "Update user",
            description = "Updates an existing user's information. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = NguoiDungResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNguoiDung(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody NguoiDungUpdateDTO updateDTO) {
        try {
            NguoiDungResponseDTO responseDTO = nguoiDungService.updateNguoiDung(id, updateDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            } else if (e.getMessage().contains("already exists")) {
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user from the system by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNguoiDung(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        try {
            nguoiDungService.deleteNguoiDung(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
