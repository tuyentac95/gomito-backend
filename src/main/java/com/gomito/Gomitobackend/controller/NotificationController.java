package com.gomito.Gomitobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class NotificationConroller {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/notify/{users}")
    public void sendNotification(@DestinationVariable String users, Message message) {
        System.out.println("Handling notification " + message + " to users: " + users);
        simpMessagingTemplate.convertAndSend("/topic/notify/" + users, message);
    }
}
