package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GListRepository extends JpaRepository<GList, Long> {
}
