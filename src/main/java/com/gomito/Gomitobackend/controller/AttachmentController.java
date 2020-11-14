package com.gomito.Gomitobackend.controller;

import com.gomito.Gomitobackend.model.Attachment;
import com.gomito.Gomitobackend.model.AttachmentDto;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.service.AttachmentService;
import com.gomito.Gomitobackend.service.GCardService;
import com.gomito.Gomitobackend.service.GListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/attachments")
@CrossOrigin("*")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;


    @Autowired
    private GCardService gCardService;

    @PostMapping("/")
    public ResponseEntity<Attachment> createAttachment(@RequestBody AttachmentDto attachmentDto){
        Attachment attachment = new Attachment();
        attachment.setAttachmentName(attachmentDto.getAttachmentName());
        GCard card = gCardService.findById(attachmentDto.getCardId());
        attachment.setCard(card);
        attachment.setUrl(attachmentDto.getAttachmentUrl());
        Attachment newAttachment = attachmentService.saveAttachment(attachment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAttachment);
    }

    @PutMapping("/update")
    public ResponseEntity<Attachment> updateAttachment(@RequestBody Attachment attachment){
        Attachment attach = attachmentService.findAttachmentById(attachment.getAttachmentId());
        attach.setAttachmentName(attachment.getAttachmentName());
        Attachment update = attachmentService.saveAttachment(attach);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Attachment> deleteAttachment(@PathVariable Long id){
        attachmentService.deleteAttachment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
