package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.service.AuthService;
import com.gomito.Gomitobackend.service.GBoardService;
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

    @GetMapping("/{id}")
    public ResponseEntity<List<GBoard>> findAllBoardByUserId(@PathVariable Long id) {
        GUser user = authService.getCurrentUser();
        if (user.getUserId().equals(id)) {
            List<GBoard> gboards = gBoardService.findAllBoardByUserId(id);
            return ResponseEntity.status(HttpStatus.OK).body(gboards);
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

}
