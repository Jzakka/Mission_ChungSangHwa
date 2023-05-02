package com.ll.gramgram.boundedContext.notification.service;

import com.ll.TestUtil;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class NotificationServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    LikeablePersonService likeablePersonService;

    @Autowired
    LikeablePersonRepository likeablePersonRepository;

    @PersistenceContext
    EntityManager em;

    @TestConfiguration
    static class TestConfig{
        @Bean
        public TestUtil testUtil() {
            return new TestUtil();
        }
    }

    @Autowired
    TestUtil testUtil;

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

    @Test
    void user3가_user4를_좋아하는_이유가_바뀜() {
        Member user3 = memberService.findByUsername("user3").get();
        Member user4 = memberService.findByUsername("user4").get();

        LikeablePerson likeablePerson = likeablePersonService
                .findByFromInstaMember_usernameAndToInstaMember_username(
                        user3.getInstaMember().getUsername(), user4.getInstaMember().getUsername()).get();
        testUtil.changeModifyDateForce(likeablePerson.getId(), likeablePersonRepository, em);

        likeablePersonService.like(user3, user4.getInstaMember().getUsername(), 2);

        List<Notification> notifications = notificationService.findByToInstaMember(user4.getInstaMember());
        assertThat(notifications).hasSize(2);
        assertThat(notifications.get(1).getOldAttractiveTypeCode()).isEqualTo(1);
        assertThat(notifications.get(1).getNewAttractiveTypeCode()).isEqualTo(2);
    }
}