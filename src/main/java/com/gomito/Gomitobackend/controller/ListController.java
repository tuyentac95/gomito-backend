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
@RequestMapping("/api/lists")
@CrossOrigin("*")
public class ListController {
    @Autowired
    GBoardService gBoardService;

    @Autowired
    GCardService gCardService;

    @Autowired
    GListService gListService;

    @Autowired
    AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GCard>> findAllCardByListId(@PathVariable Long id) {
        List<GCard> cards = gCardService.findALlByListIdAndOrderByCardIndex(id);
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @PostMapping("/")
    public ResponseEntity<GList> createList(@RequestBody GListDto listDto) {
        GList glist = new GList();
        glist.setListName(listDto.getListName());
        GBoard gBoard = gBoardService.findById(listDto.getBoardId());
        glist.setBoard(gBoard);

        Integer maxIndex = gListService.findMaxIndex(listDto.getBoardId());
        glist.setListIndex(maxIndex + 1);
        GList gList = gListService.save(glist);
        return ResponseEntity.status(HttpStatus.CREATED).body(gList);
    }

    @PostMapping("/updateIndex")
    public ResponseEntity<String> updateListIndex(@RequestBody List<GListDto> updateLists) {
        System.out.println(updateLists);
        Long boardId = getBoardId(updateLists);
        System.out.println("Check boardId " + boardId);
        if (boardId > 0) {
            System.out.println("check user id: " + checkUserId(boardId));
            if (checkUserId(boardId)) {
                for (GListDto list : updateLists) {
                    System.out.println("check list " + list);
                    GList gList = gListService.findById(list.getListId());
                    gList.setListIndex(list.getListIndex());
                    System.out.println("check glist: " + gList);
                    GList saveList = gListService.save(gList);
                    System.out.println("check save listL " + saveList);
                }
                return ResponseEntity.status(HttpStatus.OK).body("Updated successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have authorization to modify!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't have authorization to modify!");
    }

    private Long getBoardId(List<GListDto> updateLists) {
        GList gList = gListService.findById(updateLists.get(0).getListId());
        Long boardId = gList.getBoard().getBoardId();
        for (GListDto list:  updateLists){
            GList gListNew = gListService.findById(list.getListId());
            Long boardIdNew = gListNew.getBoard().getBoardId();
            if (!boardIdNew.equals(boardId)) {
                return -1L;
            }
        }
        return boardId;
    }

    private boolean checkUserId(Long boardId) {
        GBoard gBoard = gBoardService.findById(boardId);
        GUser checkUser = gBoard.getUser();
        GUser authUser = authService.getCurrentUser();
        return (checkUser.getUserId().equals(authUser.getUserId()));
    }
}
