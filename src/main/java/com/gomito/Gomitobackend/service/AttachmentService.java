package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.model.Attachment;
import com.gomito.Gomitobackend.repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public Attachment saveAttachment(Attachment attachment){
        return attachmentRepository.save(attachment);
    }
}
