package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GList;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

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

    @Autowired
    private GUserService gUserService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GList>> findAllListByBoardId(@PathVariable Long id) {
        GUser currentUser = authService.getCurrentUser();

        if (gUserService.checkMemberOfBoard(currentUser, id)) {
            List<GList> lists = gListService.findAllByBoardAndOrderByListIndex(id);
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<GBoard> createBoard(@RequestBody GBoard gBoard) {
        System.out.println("Creating Board: " + gBoard.getBoardName());
        GBoard newBoard = gBoardService.save(gBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBoard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GBoard> updateBoard(@PathVariable Long id, @RequestBody GBoard gBoard) {
        System.out.println("Update Board: " + gBoard.getBoardName());
        GBoard currentBoard = gBoardService.findById(id);
        if (currentBoard == null) {
            System.out.println("Board with id:" + id + "not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        currentBoard.setBoardName(gBoard.getBoardName());
        currentBoard.setBoardId(gBoard.getBoardId());
        gBoardService.save(gBoard);
        return new ResponseEntity<>(currentBoard, HttpStatus.OK);
    }

    @PostMapping("/{boardId}/add-member")
    public ResponseEntity<String> addMember(@PathVariable Long boardId, @RequestBody GUser member) {
        System.out.println("check input boardId: " + boardId);
        System.out.println("check input user:" + member);
        GUser newMember;
        if (member.getUsername() != null) {
            newMember = gUserService.findUserByName(member.getUsername());
        } else if (member.getEmail() != null) {
            newMember = gUserService.findUserByEmail(member.getEmail());
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and Email must not be null");
        if (newMember != null) {
            System.out.println("check member: " + newMember);
            if (gBoardService.addMember(member, boardId)) {
                return ResponseEntity.status(HttpStatus.OK).body("Waiting for your member confirmation");
            } else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Something wrong");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find your member");

    }
}
