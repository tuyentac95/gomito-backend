package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
