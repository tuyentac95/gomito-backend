package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.model.Notification;
import com.gomito.Gomitobackend.repository.GUserRepository;
import com.gomito.Gomitobackend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
@Autowired
    NotificationRepository notificationRepository;
@Autowired
    GUserRepository gUserRepository;
@Autowired
AuthService authService;

public List<Notification>findAllNotificationOfCurrentUser() {
    GUser user = authService.getCurrentUser();
    return user != null ? user.getNotifications() : null;
}

public Notification findById(Long notificationId) {
    return notificationRepository.findById(notificationId).orElse(null);
}
public Notification save(Notification notification) {
    GUser currentUser = authService.getCurrentUser();
    Notification notifica = notificationRepository.save(notification);
    List<Notification> notifications = currentUser.getNotifications();
    notifications.add(notifica);
    currentUser.setNotifications(notifications);
    gUserRepository.save(currentUser);
    return notifica;
}

}
