package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GCardRepository extends JpaRepository<GCard, Long> {
}
