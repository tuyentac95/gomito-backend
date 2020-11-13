package com.gomito.Gomitobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.gomito.Gomitobackend.dto.NotificationDto;
import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.service.AuthService;
import com.gomito.Gomitobackend.service.GCardService;
import com.gomito.Gomitobackend.service.GUserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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
    private AuthService authService;

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
                System.out.println("Send notification to " + receiver.getUsername());
                simpMessagingTemplate.convertAndSend("/topic/notify/" + receiver.getUsername(), dto);
            }
        }
    }
}
