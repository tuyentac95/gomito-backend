package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GCardDto;
import com.gomito.Gomitobackend.model.GList;
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
    private GListService gListService;

    @Autowired
    private GCardService gCardService;

    @PostMapping("/")
        public ResponseEntity<GCard> createCard(@RequestBody GCardDto cardDto) {
            GCard gcard = new GCard();
            gcard.setCardName(cardDto.getCardName());
            GList gList = gListService.findById(cardDto.getListId());
            gcard.setList(gList);

            Integer maxIndex = gCardService.findMaxIndex(cardDto.getListId());
            gcard.setCardIndex(maxIndex+1);
            GCard gCard = gCardService.save(gcard);
            return ResponseEntity.status(HttpStatus.CREATED).body(gCard);
        }

        @PostMapping("/updateIndex")
    public ResponseEntity<String> updateCardIndex(@RequestBody List<GCardDto> updateCards){
        Long listId = getListId(updateCards);
        if (listId > 0){
            Long boardId = getBoardId(listId);
            if(boardId > 0){

            }
        }
        }

//    private Long getBoardId(Long listId) {
//        GCard gCard = gCardService.findById(updateCards.get(0).getCardId());
//        Long listId = gCard.getList().getListId();
//        for (GCardDto card: updateCards){
//            GCard gCardNew = gCardService.findById(card.getCardId());
//            Long listIdNew = gCardNew.getList().getListId();
//            if (!listIdNew.equals(listId)){
//                return -1L;
//            }
//        }
//        return listId;
//    }

    private Long getListId(List<GCardDto> updateCards) {
        GCard gCard = gCardService.findById(updateCards.get(0).getCardId());
        Long listId = gCard.getList().getListId();
        for (GCardDto card: updateCards){
            GCard gCardNew = gCardService.findById(card.getCardId());
            Long listIdNew = gCardNew.getList().getListId();
            if (!listIdNew.equals(listId)){
                return -1L;
            }
        }
        return listId;
    }
}
