package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.JoinGroupToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JoinGroupTokenRepository extends JpaRepository<JoinGroupToken, Long> {
    Optional<JoinGroupToken> findByToken(String token);
}
