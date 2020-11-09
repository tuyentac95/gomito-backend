package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.GLabel;
import com.gomito.Gomitobackend.model.GLabelDto;
import com.gomito.Gomitobackend.model.GList;
import com.gomito.Gomitobackend.service.GLabelService;
import com.gomito.Gomitobackend.service.GListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/labels")
@CrossOrigin("*")
public class LabelController {
    @Autowired
    GLabelService gLabelService;

    @Autowired
    GListService gListService;

    @PostMapping("/")
    public ResponseEntity<GLabel> createLabel(@RequestBody GLabelDto gLabelDto){
        GLabel gLabel = new GLabel();
        gLabel.setLabelName(gLabelDto.getLabelName());

        GList gList = gListService.findById(gLabelDto.getListId());
        gLabel.setList(gList);

        GLabel label = gLabelService.save(gLabel);
        return ResponseEntity.status(HttpStatus.CREATED).body(label);
    }
}
