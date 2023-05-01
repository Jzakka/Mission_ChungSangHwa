package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    LikeablePersonService likeablePersonService;

    @Test
    void user2가_user3_를_좋아함() {
        Member user2 = memberService.findByUsername("user2").get();
        Member user3 = memberService.findByUsername("user3").get();

        likeablePersonService.like(user2, user3.getInstaMember().getUsername(), 1);

        List<Notification> notifications = notificationService.findByToInstaMember(user3.getInstaMember());
        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getFromInstaMember()).isEqualTo(user2.getInstaMember());
        assertThat(notifications.get(0).getNewAttractiveTypeCode()).isEqualTo(1);
    }
}