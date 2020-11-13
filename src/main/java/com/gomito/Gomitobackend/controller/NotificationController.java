package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.NotificationDto;
import com.gomito.Gomitobackend.model.*;
import com.gomito.Gomitobackend.repository.GBoardRepository;
import com.gomito.Gomitobackend.repository.GCardRepository;
import com.gomito.Gomitobackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GCardService cardService;

    @Autowired
    private GUserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationStatusService statusService;

    @MessageMapping("/notifyAll/{cardId}")
    public void sendNotificationToAll(@DestinationVariable Long cardId, NotificationDto dto) {
        GCard card = cardService.findById(cardId);
        if (card != null) {
            System.out.println("check card " + card.getCardName());
            GBoard board = card.getList().getBoard();
            GUser currentUser = userService.findUserByName(dto.getSenderName());
            if (userService.checkMemberOfBoard(currentUser, board.getBoardId())) {
                List<GUser> members = board.getUsers();

                for (GUser anotherMember : members) {
                    if (!anotherMember.getUsername().equals(dto.getSenderName())) {
                        System.out.println("Saving notification of " + anotherMember.getUsername());
                        Notification newNotification = new Notification();
                        newNotification.setNotificationName(dto.getMessage());
                        newNotification.setSender(currentUser);
                        newNotification.setCreatedDate(Instant.now());
                        NotificationStatus status = new NotificationStatus();
                        status.setReceiver(anotherMember);
                        status.setNotification(notificationService.save(newNotification));
                        status.setStatus(0);
                        statusService.save(status);
                        System.out.println("Send notification to " + anotherMember.getUsername());
                        simpMessagingTemplate.convertAndSend("/topic/notify/" + anotherMember.getUsername(), dto);
                    }
                }
            }
        }
    }

    @MessageMapping("/notifyOne/{cardId}")
    public void sendNotificationToOne(@DestinationVariable Long cardId, NotificationDto dto) {
        GCard card = cardService.findById(cardId);
        if (card != null) {
            System.out.println("check card " + card.getCardName());
            GBoard board = card.getList().getBoard();
            GUser sender = userService.findUserByName(dto.getSenderName());
            GUser receiver = userService.findUserByName(dto.getReceiverName());
            if (userService.checkMemberOfBoard(sender, board.getBoardId()) && userService.checkMemberOfBoard(receiver, board.getBoardId())) {
                System.out.println("Saving notification of " + receiver.getUsername());
                Notification newNotification = new Notification();
                newNotification.setSender(sender);
                newNotification.setNotificationName(dto.getMessage());
                newNotification.setCreatedDate(Instant.now());
                NotificationStatus status = new NotificationStatus();
                status.setReceiver(receiver);
                status.setStatus(0);
                status.setNotification(notificationService.save(newNotification));
                statusService.save(status);
                System.out.println("Send notification to " + receiver.getUsername());
                simpMessagingTemplate.convertAndSend("/topic/notify/" + receiver.getUsername(), dto);
            }
        }
    }

}
