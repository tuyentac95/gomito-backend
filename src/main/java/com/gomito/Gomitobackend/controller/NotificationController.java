package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/notify/{cardId}")
    public void sendNotification(@DestinationVariable Long cardId, Notification notification){
        System.out.println("Handling notification" + notification + " to group: " + cardId);
        simpMessagingTemplate.convertAndSend("/topic/notify/" + cardId, notification);
    }
}
