package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
