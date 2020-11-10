package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.GListDto;
import com.gomito.Gomitobackend.model.*;
import com.gomito.Gomitobackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @Autowired
    GUserService gUserService;

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
        if (boardId > 0) {
            GUser currentUser = authService.getCurrentUser();
            if (gUserService.checkMemberOfBoard(currentUser, boardId)) {
                for (GListDto list : updateLists) {
                    GList gList = gListService.findById(list.getListId());
                    gList.setListIndex(list.getListIndex());
                    gListService.save(gList);
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

    @PutMapping("/update")
    public ResponseEntity<GList> saveList(@RequestBody GList gList){
        GList list = gListService.findById(gList.getListId());
        list.setListName(gList.getListName());
        GList updateList = gListService.save(list);
        return new ResponseEntity<>(updateList, HttpStatus.OK);
    }
}
