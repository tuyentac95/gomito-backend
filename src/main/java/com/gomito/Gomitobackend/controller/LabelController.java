package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.dto.GLabelDto;
import com.gomito.Gomitobackend.model.*;
import com.gomito.Gomitobackend.service.GBoardService;
import com.gomito.Gomitobackend.service.GLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/labels")
@CrossOrigin("*")
public class LabelController {
    @Autowired
    GLabelService gLabelService;

    @Autowired
    GBoardService gBoardService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GLabel>> findAllLabelByBoardId(@PathVariable Long id) {
        List<GLabel> labels = gLabelService.findAllLabelByBoardId(id);
        return ResponseEntity.status(HttpStatus.OK).body(labels);
    }

    @PostMapping("/")
    public ResponseEntity<GLabel> createLabel(@RequestBody GLabelDto gLabelDto){
        GLabel gLabel = new GLabel();
        gLabel.setLabelName(gLabelDto.getLabelName());
        gLabel.setColor(gLabelDto.getColor());

        GBoard gBoard = gBoardService.findById(gLabelDto.getBoardId());
        gLabel.setBoard(gBoard);

        GLabel label = gLabelService.save(gLabel);
        return ResponseEntity.status(HttpStatus.CREATED).body(label);
    }
}