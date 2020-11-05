package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.service.GCardService;
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
    GCardService gCardService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GCard>> findAllCardByListId(@PathVariable Long id) {
        List<GCard> cards = gCardService.findAllCardByListId(id);
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }
}
