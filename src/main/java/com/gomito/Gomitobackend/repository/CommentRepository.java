package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.Comment;
import com.gomito.Gomitobackend.model.GCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long > {
    List<Comment> findAllByCard(GCard gCard);
}
