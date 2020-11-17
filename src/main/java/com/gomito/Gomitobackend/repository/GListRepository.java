package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GBoard;
import com.gomito.Gomitobackend.model.GList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GListRepository extends JpaRepository<GList, Long> {
    List<GList> findAllByBoard(GBoard board);
    List<GList> findAllByBoardOrderByListIndex(GBoard board);
}
