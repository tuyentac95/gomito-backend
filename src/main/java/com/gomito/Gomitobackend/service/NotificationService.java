package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.dto.NotificationDto;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.model.Notification;
import com.gomito.Gomitobackend.model.NotificationStatus;
import com.gomito.Gomitobackend.repository.GUserRepository;
import com.gomito.Gomitobackend.repository.NotificationRepository;
import com.gomito.Gomitobackend.repository.NotificationStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    private NotificationStatusRepository statusRepository;

    @Autowired
    GUserRepository gUserRepository;

    @Autowired
    AuthService authService;

    public List<NotificationDto> findAllNotificationsOfCurrentUser() {
        GUser user = authService.getCurrentUser();
        List<NotificationStatus> statuses = statusRepository.findAllByReceiverOrderByStatusIdDesc(user);
        List<NotificationDto> dtos = new ArrayList<>();
        for (NotificationStatus status : statuses) {
            Notification notification = status.getNotification();
            NotificationDto dto = new NotificationDto();
            dto.setNotificationId(status.getStatusId());
            dto.setMessage(notification.getNotificationName());
            dto.setSenderName(notification.getSender().getUsername());
            dto.setStatus(status.getStatus());
            dtos.add(dto);
        }
        return dtos;
    }

    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElse(null);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
