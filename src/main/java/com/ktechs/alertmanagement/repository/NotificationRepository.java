package com.ktechs.alertmanagement.repository;

import com.ktechs.alertmanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find the most recent notification by user_id
    @Query(value = "SELECT n FROM Notification n WHERE n.user_id = :userId ORDER BY n.timestamp DESC")
    List<Notification> findRecentByUserId(@Param("userId") Integer userId);
}

