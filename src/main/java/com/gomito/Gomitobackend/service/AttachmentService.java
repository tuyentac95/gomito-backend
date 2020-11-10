package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.model.Attachment;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final GCardService gCardService;

    public Attachment saveAttachment(Attachment attachment){
        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAttachmentList(Long id){
        GCard card = gCardService.findById(id);
        return attachmentRepository.findAllByCard(card);

    }
}
