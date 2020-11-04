package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GBoardRepository extends JpaRepository<GBoard, Long> {
    List<GBoard> findAllByUser(GUser user);
}
