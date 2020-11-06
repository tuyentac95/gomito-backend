package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GList;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.service.AuthService;
import com.gomito.Gomitobackend.service.GBoardService;
import com.gomito.Gomitobackend.service.GCardService;
import com.gomito.Gomitobackend.service.GListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin("*")
public class BoardController {

    @Autowired
    private GBoardService gBoardService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GListService gListService;

    @Autowired
    private GCardService gCardService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GList>> findAllListByBoardId(@PathVariable Long id) {
        GBoard board = gBoardService.findById(id);
        GUser user = board.getUser();
        GUser currentUser = authService.getCurrentUser();

        if (user.getUserId().equals(currentUser.getUserId())) {
            List<GList> lists = gListService.findAllListByBoardId(id);
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Void> createBoard(@RequestBody GBoard gBoard, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Board: " + gBoard.getBoardName());
        gBoardService.save(gBoard);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/boards/{id}").buildAndExpand(gBoard.getBoardId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GBoard> updateBoard(@PathVariable Long id, @RequestBody GBoard gBoard) {
        System.out.println("Update Board: " + gBoard.getBoardName());
        GBoard currentBoard = gBoardService.findById(id);
        if (currentBoard == null) {
            System.out.println("Board with id:" + id + "not found");
            return new ResponseEntity<GBoard>(currentBoard, HttpStatus.NOT_FOUND);
        }
        currentBoard.setBoardName(gBoard.getBoardName());
        currentBoard.setBoardId(gBoard.getBoardId());
        gBoardService.save(gBoard);
        return new ResponseEntity<GBoard>(currentBoard, HttpStatus.OK);
    }

}
