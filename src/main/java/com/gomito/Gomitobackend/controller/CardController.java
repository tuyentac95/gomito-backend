package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.GCardDto;
import com.gomito.Gomitobackend.model.*;
import com.gomito.Gomitobackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private AttachmentService attachmentService;

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

//    private boolean checkUserId(Long boardIdNew) {
//        GBoard gBoard = gBoardService.findById(boardIdNew);
//        GUser checkUser = gBoard.getUser();
//        GUser authUser = authService.getCurrentUser();
//        return checkUser.getUserId().equals(authUser.getUserId());
//    }

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
    public ResponseEntity<String> changeIndexOfCard(@RequestBody List<GCardDto> listGCarDto){
        for (GCardDto cardDto: listGCarDto){
            GCard gCard = gCardService.findById(cardDto.getCardId());
            GList gList = gListService.findById(cardDto.getListId());
            gCard.setList(gList);
            gCard.setCardIndex(cardDto.getCardIndex());
            gCardService.save(gCard);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Updated successfully!");
    }

    @PostMapping("/addLabelToCard/{labelId}")
    public ResponseEntity<String> addLabelToCard(@PathVariable Long labelId,@RequestBody GCardDto gCardDto){
        GCard gCard = gCardService.findById(gCardDto.getCardId());
        Set<GLabel> listlabels =  gCard.getLabels();
        GLabel label = gLabelService.findById(labelId);
        listlabels.add(label);
        gCardService.save(gCard);
        return ResponseEntity.status(HttpStatus.OK).body("Updated successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Attachment>> getAttachmentList(@PathVariable Long id) {
        List<Attachment> attachments = attachmentService.getAttachmentList(id);
        return ResponseEntity.status(HttpStatus.OK).body(attachments);
    }
}
