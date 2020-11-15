package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.CommentDto;
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

    @Autowired
    CommentService commentService;

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
    public ResponseEntity<String> addLabelToCard(@PathVariable Long labelId, @RequestBody GCardDto gCardDto) {
        GCard gCard = gCardService.findById(gCardDto.getCardId());
        List<GLabel> listLabels = gCard.getLabels();
        GLabel label = gLabelService.findById(labelId);
        if (listLabels.contains(label)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        listLabels.add(label);
        gCard.setLabels(listLabels);
        System.out.println("check 3");
        gCardService.save(gCard);
        System.out.println("Vừa thêm label có id là: " + labelId);
        return ResponseEntity.status(HttpStatus.OK).body("Updated done!");
    }

    @GetMapping("/attachment/{id}")
    public ResponseEntity<List<AttachmentDto>> getAttachmentList(@PathVariable Long id) {
        List<Attachment> attachments = attachmentService.getAttachmentList(id);
        List<AttachmentDto> dtos = new ArrayList<>();
        for (Attachment at : attachments) {
            AttachmentDto dto = new AttachmentDto();
            dto.setAttachmentId(at.getAttachmentId());
            dto.setAttachmentName(at.getAttachmentName());
            dto.setAttachmentUrl(at.getUrl());
            dto.setCardId(at.getCard().getCardId());
            dtos.add(dto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GCardDto> getCard(@PathVariable Long id) {
        GCard card = gCardService.findCardById(id);
        if (card != null) {
            GCardDto cardDto = new GCardDto();
            cardDto.setCardId(card.getCardId());
            cardDto.setCardName(card.getCardName());
            cardDto.setDescription(card.getDescription());
            cardDto.setListId(id);
            cardDto.setCardIndex(card.getCardIndex());
            cardDto.setLabels(card.getLabels());
            cardDto.setMembers(card.getUsers());
            return ResponseEntity.status(HttpStatus.OK).body(cardDto);
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
    public ResponseEntity<List<GCardDto>> searchByNamedParams(@PathVariable String name) {
        GUser currentUser = authService.getCurrentUser();
        List<GCard> cards = gCardService.searchByName(name);
        List<GCardDto> response = new ArrayList<>();
        for (GCard card : cards) {
            GBoard board = card.getList().getBoard();
            if (gUserService.checkMemberOfBoard(currentUser, board.getBoardId())) {
                GCardDto cardDto = new GCardDto();
                cardDto.setCardId(card.getCardId());
                cardDto.setCardName(card.getCardName());
                cardDto.setDescription(card.getDescription());
                cardDto.setListId(card.getList().getListId());
                cardDto.setBoardId(card.getList().getBoard().getBoardId());
                response.add(cardDto);
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

    @GetMapping("/{cardId}/get-members")
    public ResponseEntity<List<GUserDto>> getMembers(@PathVariable Long cardId) {
        GCard card = gCardService.findById(cardId);
        if (card == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        GUser currentUser = authService.getCurrentUser();
        if (!gUserService.checkMemberOfBoard(currentUser, card.getList().getBoard().getBoardId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        List<GUser> users = card.getUsers();
        List<GUserDto> cardMembers = new ArrayList<>();
        for (GUser user : users) {
            GUserDto member = new GUserDto();
            member.setUserId(user.getUserId());
            member.setUsername(user.getUsername());
            member.setEmail(user.getEmail());
            member.setAvatarUrl(user.getAvatarUrl());
            cardMembers.add(member);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cardMembers);
    }

    @GetMapping("/writeComment/{cardId}")
    public ResponseEntity<List<CommentDto>> writeComment(@PathVariable Long cardId) {
        GCard card = gCardService.findById(cardId);
        Long boardId = card.getList().getBoard().getBoardId();
        if (boardId > 0) {
            GUser currentUser = authService.getCurrentUser();
            if (gUserService.checkMemberOfBoard(currentUser, boardId)) {
                List<Comment> comments = commentService.findAllByCardId(cardId);
                List<CommentDto> commentDtos = new ArrayList<>();
                for (Comment newComment : comments) {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setCommentId(newComment.getCommentId());
                    commentDto.setContent(newComment.getContent());
                    commentDto.setCardId(cardId);
                    commentDto.setUser(newComment.getUser());
                    commentDtos.add(commentDto);
                }
                return ResponseEntity.status(HttpStatus.OK).body(commentDtos);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
