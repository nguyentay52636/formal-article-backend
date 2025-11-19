package org.example.repository;

import org.example.entity.ChatMessage;
import org.example.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomOrderByCreatedAtAsc(ChatRoom room);
}
