package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GCardRepository extends JpaRepository<GCard, Long> {
    List<GCard> findAllByList(GList gList);
    List<GCard> findAllByListOrderByCardIndex(GList gList);
}
