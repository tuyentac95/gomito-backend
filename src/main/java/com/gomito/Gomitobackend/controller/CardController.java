package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.GCardDto;
import com.gomito.Gomitobackend.dto.GUserDto;
import com.gomito.Gomitobackend.model.*;
import com.gomito.Gomitobackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin("*")
public class CardController {
    @Autowired
    GBoardService gBoardService;

    @Autowired
    private GListService gListService;

    @Autowired
    private GLabelService gLabelService;

    @Autowired
    private GCardService gCardService;

    @Autowired
    AuthService authService;

    @Autowired
    GUserService gUserService;

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
                GUser currentUser = authService.getCurrentUser();
                if (gUserService.checkMemberOfBoard(currentUser, boardIdNew)) {
                    for (GCardDto card : updateCards) {
                        GCard gCard = gCardService.findById(card.getCardId());
                        gCard.setCardIndex(card.getCardIndex());
                        gCardService.save(gCard);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body("Updated successfully!");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have authorization to modify!");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have authorization to modify!");
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

    @PostMapping("updateIndexOfCardInAnotherList")
    public ResponseEntity<String> changeIndexOfCard(@RequestBody List<GCardDto> listGCarDto) {
        for (GCardDto cardDto : listGCarDto) {
            GCard gCard = gCardService.findById(cardDto.getCardId());
            GList gList = gListService.findById(cardDto.getListId());
            gCard.setList(gList);
            gCard.setCardIndex(cardDto.getCardIndex());
            gCardService.save(gCard);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Updated successfully!");
    }

    @PostMapping("/addLabelToCard/{labelId}")
    public ResponseEntity<String> addLabelToCard(@PathVariable Long labelId, @RequestBody GCardDto gCardDto) {
        GCard gCard = gCardService.findById(gCardDto.getCardId());
        Set<GLabel> listlabels = gCard.getLabels();
        GLabel label = gLabelService.findById(labelId);
        listlabels.add(label);
        gCardService.save(gCard);
        return ResponseEntity.status(HttpStatus.OK).body("Updated successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<GCard> getCard(@PathVariable Long id) {
        GCard card = gCardService.findCardById(id);
        if (card != null) {
            return ResponseEntity.status(HttpStatus.OK).body(card);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<GCard> saveCard(@RequestBody GCardDto gCardDto) {
        GCard card = gCardService.findById(gCardDto.getCardId());
        card.setCardName(gCardDto.getCardName());
        card.setDescription(gCardDto.getDescription());
        GCard updateCard = gCardService.save(card);
        return new ResponseEntity<>(updateCard, HttpStatus.OK);
    }

    @GetMapping("/searches/{name}")
    public ResponseEntity<List<GCard>> searchByNamedParams(@PathVariable String name) {
        GUser currentUser = authService.getCurrentUser();
        List<GCard> cards = gCardService.searchByName(name);
        List<GCard> response = new ArrayList<>();
        for (GCard card : cards) {
            GBoard board = card.getList().getBoard();
            if (gUserService.checkMemberOfBoard(currentUser, board.getBoardId())) {
                response.add(card);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/{cardId}/add-member")
    public ResponseEntity<String> addMember(@PathVariable Long cardId, @RequestBody GUserDto member) {
        GBoard board = gBoardService.findByCardId(cardId);
        GUser newMember = gUserService.findById(member.getUserId());
        if (board != null && newMember != null) {
            GUser currentUser = authService.getCurrentUser();
            Long boardId = board.getBoardId();
            if (gUserService.checkMemberOfBoard(currentUser, boardId)
                    && gUserService.checkMemberOfBoard(newMember, boardId)) {
                if (gCardService.addMember(newMember, cardId)) {
                    return ResponseEntity.status(HttpStatus.OK).body("Add member to card successful");
                }
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Something's wrong");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something's wrong");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something's wrong");
    }
}
