package com.ll.gramgram.boundedContext.notification.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToInstaMemberAndReadDateIsNull(InstaMember toInstaMember);

    @Modifying
    @Query("""
            update Notification n
            set n.readDate = CURRENT_TIMESTAMP
            where n.toInstaMember=:toInstaMember and n.readDate is null 
            """)
    void updateReadDateByToInstaMember(@Param("toInstaMember") InstaMember toInstaMember);

}