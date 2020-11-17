package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GCard;
import com.gomito.Gomitobackend.model.GList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GCardRepository extends JpaRepository<GCard, Long> {
    List<GCard> findAllByList(GList gList);
    List<GCard> findAllByListOrderByCardIndex(GList gList);
    Optional<GCard> findFirstByListOrderByCardIndexDesc(GList list);

    @Query("SELECT card FROM GCard card WHERE card.cardName like %:search% or card.description like %:search%")
    List<GCard> findCardByNamedParams(@Param("search") String search);

}
