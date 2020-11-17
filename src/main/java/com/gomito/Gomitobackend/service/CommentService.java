package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.model.Comment;
import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    GCardService gCardService;

    public List<Comment> findAllByCardId(Long cardId){
        GCard gCard = gCardService.findById(cardId);
        return commentRepository.findAllByCard(gCard);
    }

    public Comment save(Comment comment) {return commentRepository.save(comment);}

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment findById(Long commentId){
        return commentRepository.findById(commentId).orElse(null);
    }
}
