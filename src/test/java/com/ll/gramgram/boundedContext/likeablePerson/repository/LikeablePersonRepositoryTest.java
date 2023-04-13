package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LikeablePersonRepositoryTest {

    @Autowired
    private LikeablePersonRepository repository;

    @Test
    void 상대이름으로_조회() {
        List<LikeablePerson> likeablePeople = repository.findByToInstaMember_Username("insta_user100");
        Assertions.assertThat(likeablePeople.get(0).getId()).isEqualTo(2);
        Optional<LikeablePerson> likeablePersonOptional = repository
                .findByFromInstaMemberIdAndToInstaMember_Username(2l,"insta_user100");
        Assertions.assertThat(likeablePersonOptional.isPresent()).isTrue();
    }
}