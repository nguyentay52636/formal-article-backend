package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.roomChat.CreateRoomRequest;
import org.example.dto.request.roomChat.UpdateRoomRequest;
import org.example.dto.response.roomChat.RoomChatResponse;
import org.example.entity.ChatRoom;
import org.example.mapper.RoomChatMapper;
import org.example.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/room-chats")
@Tag(name = "Phòng Chat (Room Chat)", description = "API quản lý phòng chat")
@RequiredArgsConstructor
public class RoomChatController {

    private final ChatService chatService;
    private final RoomChatMapper roomChatMapper;

    @PostMapping
    @Operation(summary = "Tạo phòng chat mới", description = "Tạo một phòng chat mới giữa user và admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tạo thành công"),
        @ApiResponse(responseCode = "404", description = "User hoặc Admin không tồn tại")
    })
    public ResponseEntity<RoomChatResponse> createRoom(@RequestBody CreateRoomRequest request) {
        ChatRoom room = chatService.createRoom(request);
        return ResponseEntity.ok(roomChatMapper.toResponse(room));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Duyệt phòng chat", description = "Admin duyệt yêu cầu tạo phòng chat và được assign vào room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Duyệt thành công"),
        @ApiResponse(responseCode = "404", description = "Phòng chat không tồn tại"),
        @ApiResponse(responseCode = "400", description = "Room không ở trạng thái pending hoặc user không phải admin")
    })
    public ResponseEntity<RoomChatResponse> approveRoom(
            @Parameter(description = "ID của phòng chat", required = true) @PathVariable String id,
            @Parameter(description = "ID của admin đang approve", required = true) @RequestParam Long adminId) {
        ChatRoom room = chatService.approveRoom(id, adminId);
        return ResponseEntity.ok(roomChatMapper.toResponse(room));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin phòng chat", description = "Lấy chi tiết phòng chat theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công"),
        @ApiResponse(responseCode = "404", description = "Phòng chat không tồn tại")
    })
    public ResponseEntity<RoomChatResponse> getRoomById(
            @Parameter(description = "ID của phòng chat", required = true) @PathVariable String id) {
        ChatRoom room = chatService.getRoomById(id);
        return ResponseEntity.ok(roomChatMapper.toResponse(room));
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách phòng chat", description = "Lấy tất cả các phòng chat")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<RoomChatResponse>> getAllRooms() {
        List<ChatRoom> rooms = chatService.getAllRooms();
        List<RoomChatResponse> responses = rooms.stream()
                .map(roomChatMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật phòng chat", description = "Cập nhật thông tin phòng chat (ví dụ: bật/tắt AI)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "Phòng chat không tồn tại")
    })
    public ResponseEntity<RoomChatResponse> updateRoom(
            @Parameter(description = "ID của phòng chat", required = true) @PathVariable String id,
            @RequestBody UpdateRoomRequest request) {
        ChatRoom room = chatService.updateRoom(id, request);
        return ResponseEntity.ok(roomChatMapper.toResponse(room));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa phòng chat", description = "Xóa phòng chat theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "Phòng chat không tồn tại")
    })
    public ResponseEntity<Void> deleteRoom(
            @Parameter(description = "ID của phòng chat", required = true) @PathVariable String id) {
        chatService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lấy danh sách phòng chat của user", description = "Lấy tất cả các phòng chat mà user tham gia")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<RoomChatResponse>> getRoomsByUser(
            @Parameter(description = "ID của user", required = true) @PathVariable Long userId) {
        List<ChatRoom> rooms = chatService.getRoomsByUserId(userId);
        List<RoomChatResponse> responses = rooms.stream()
                .map(roomChatMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admin/{adminId}")
    @Operation(summary = "Lấy danh sách phòng chat của admin", description = "Lấy tất cả các phòng chat mà admin quản lý")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<RoomChatResponse>> getRoomsByAdmin(
            @Parameter(description = "ID của admin", required = true) @PathVariable Long adminId) {
        List<ChatRoom> rooms = chatService.getRoomsByAdminId(adminId);
        List<RoomChatResponse> responses = rooms.stream()
                .map(roomChatMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Lấy danh sách phòng chat theo trạng thái", description = "Lấy tất cả các phòng chat có trạng thái cụ thể")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<RoomChatResponse>> getRoomsByStatus(
            @Parameter(description = "Trạng thái phòng chat (pending/active/closed/timeout)", required = true) 
            @PathVariable String status) {
        List<ChatRoom> rooms = chatService.getRoomsByStatus(status);
        List<RoomChatResponse> responses = rooms.stream()
                .map(roomChatMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/pending")
    @Operation(summary = "Lấy danh sách phòng chat đang chờ", description = "Lấy tất cả các phòng chat có trạng thái pending (dành cho admin)")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<RoomChatResponse>> getPendingRooms() {
        List<ChatRoom> rooms = chatService.getPendingRooms();
        List<RoomChatResponse> responses = rooms.stream()
                .map(roomChatMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Đóng phòng chat", description = "Đóng phòng chat (chuyển status sang closed)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Đóng thành công"),
        @ApiResponse(responseCode = "404", description = "Phòng chat không tồn tại")
    })
    public ResponseEntity<RoomChatResponse> closeRoom(
            @Parameter(description = "ID của phòng chat", required = true) @PathVariable String id) {
        ChatRoom room = chatService.closeRoom(id);
        return ResponseEntity.ok(roomChatMapper.toResponse(room));
    }
}
