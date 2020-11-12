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

    @MessageMapping("/notify/{group}")
    public void sendNotification(@DestinationVariable String group, Notification notification){
        System.out.println("Handling notification" + notification + " to group: " + group);
        simpMessagingTemplate.convertAndSend("topic/notify/" + group, notification);
    }
}
