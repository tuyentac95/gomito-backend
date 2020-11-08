package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GUserRepository extends JpaRepository<GUser, Long> {
    Optional<GUser> findByUsername(String username);
    Optional<GUser> findByEmail(String email);
}
