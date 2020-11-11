package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GLabelRepository extends JpaRepository<GLabel, Long> {
    List<GLabel> findAllByBoard(GBoard gBoard);
}
