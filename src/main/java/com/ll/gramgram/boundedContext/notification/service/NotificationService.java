package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findByToInstaMember(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMember(toInstaMember);
    }

    public void notify(LikeablePerson likeablePerson) {
        Notification notification = Notification.builder()
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .toInstaMember(likeablePerson.getToInstaMember())
                .typeCode("Like")
                .newGender(likeablePerson.getFromInstaMember().getGender())
                .build();
        notificationRepository.save(notification);
    }
}