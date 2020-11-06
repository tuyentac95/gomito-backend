package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GList;
import com.gomito.Gomitobackend.model.GListDto;
import com.gomito.Gomitobackend.service.GBoardService;
import com.gomito.Gomitobackend.service.GCardService;
import com.gomito.Gomitobackend.service.GListService;
import io.swagger.models.auth.In;
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

    @GetMapping("/{id}")
    public ResponseEntity<List<GCard>> findAllCardByListId(@PathVariable Long id) {
        List<GCard> cards = gCardService.findAllCardByListId(id);
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @PostMapping("/")
    public ResponseEntity<GList> createList(@RequestBody GListDto listDto) {
        GList glist = new GList();
        glist.setListName(listDto.getListName());
        GBoard gBoard = gBoardService.findById(listDto.getBoardId());
        glist.setBoard(gBoard);

        Integer maxIndex = gListService.findMaxIndex(listDto.getBoardId());
        glist.setListIndex(maxIndex+1);
        GList gList = gListService.save(glist);
        return ResponseEntity.status(HttpStatus.CREATED).body(gList);
    }
}
