package org.example.repository;

import org.example.entity.Notification;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);
    List<Notification> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(User receiver);
    void deleteByReceiver(User receiver);
}
