package org.example.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // In a real app, you would get the user from the principal or session attributes
        // For now, we log the connection and notify admins
        log.info("Received a new web socket connection");
        
        // Notify admins that a user has connected (you might want to send user details here)
        // Assuming we send a simple message or object
        messagingTemplate.convertAndSend("/topic/admin/notifications", Map.of(
            "type", "CONNECT",
            "message", "New user connected"
        ));
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Web socket connection disconnected");
        
        // Notify admins
        messagingTemplate.convertAndSend("/topic/admin/notifications", Map.of(
            "type", "DISCONNECT",
            "message", "User disconnected"
        ));
    }
}
