package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.Attachment;
import com.gomito.Gomitobackend.model.GCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByCard(GCard card);
}
