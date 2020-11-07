package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.*;
import com.gomito.Gomitobackend.service.AuthService;
import com.gomito.Gomitobackend.service.GBoardService;
import com.gomito.Gomitobackend.service.GCardService;
import com.gomito.Gomitobackend.service.GListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin("*")
public class CardController {
    @Autowired
    GBoardService gBoardService;

    @Autowired
    private GListService gListService;

    @Autowired
    private GCardService gCardService;

    @Autowired
    AuthService authService;

    @PostMapping("/")
    public ResponseEntity<GCard> createCard(@RequestBody GCardDto cardDto) {
        GCard gcard = new GCard();
        gcard.setCardName(cardDto.getCardName());

        GList gList = gListService.findById(cardDto.getListId());
        gcard.setList(gList);

        Integer maxIndex = gCardService.findMaxIndex(cardDto.getListId());
        gcard.setCardIndex(maxIndex + 1);

        GCard card = gCardService.save(gcard);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @PostMapping("/updateIndex")
    public ResponseEntity<String> updateCardIndex(@RequestBody List<GCardDto> updateCards) {
        Long listId = getListId(updateCards);
        if (listId > 0) {
            GList gList = gListService.findById(listId);
            Long boardIdNew = gList.getBoard().getBoardId();
            if (boardIdNew > 0) {
                if (checkUserId(boardIdNew)) {
                    for (GCardDto card : updateCards) {
                        GCard gCard = gCardService.findById(card.getCardId());
                        gCard.setCardIndex(card.getCardIndex());
                        GCard saveCard = gCardService.save(gCard);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body("Updated successfully!");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have authorization to modify!");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have authorization to modify!");
    }

    private boolean checkUserId(Long boardIdNew) {
        GBoard gBoard = gBoardService.findById(boardIdNew);
        GUser checkUser = gBoard.getUser();
        GUser authUser = authService.getCurrentUser();
        return checkUser.getUserId().equals(authUser.getUserId());
    }

    private Long getListId(List<GCardDto> updateCards) {
        GCard gCard = gCardService.findById(updateCards.get(0).getCardId());
        Long listId = gCard.getList().getListId();
        for (GCardDto card : updateCards) {
            GCard gCardNew = gCardService.findById(card.getCardId());
            Long listIdNew = gCardNew.getList().getListId();
            if (!listIdNew.equals(listId)) {
                return -1L;
            }
        }
        return listId;
    }
}
