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
}
