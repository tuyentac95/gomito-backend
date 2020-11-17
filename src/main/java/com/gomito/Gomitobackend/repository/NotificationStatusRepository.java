package com.gomito.Gomitobackend.repository;

import com.gomito.Gomitobackend.model.GUser;
import com.gomito.Gomitobackend.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
    List<NotificationStatus> findAllByReceiverOrderByStatusIdDesc(GUser receiver);
}
