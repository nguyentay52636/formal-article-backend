package org.example.repository;

import org.example.entity.ChatRoom;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByUserAndAdmin(User user, User admin);
    List<ChatRoom> findByUser(User user);
    List<ChatRoom> findByAdmin(User admin);
}
