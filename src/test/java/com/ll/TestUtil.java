package com.ll;

import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class TestUtil {
    @Value("${constant.modify-delete-cooltime}")
    private Integer coolTime;

    public void changeModifyDateForce(Long id, LikeablePersonRepository repository, EntityManager em) {
        repository.changeModifyDate(id, LocalDateTime.now().minusMinutes(coolTime));
        em.refresh(repository.findById(id).get());
    }
}

