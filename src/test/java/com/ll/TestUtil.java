package com.ll;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import jakarta.persistence.EntityManager;

import java.lang.reflect.Field;


public class TestUtil {
    public static void setPrivateField(Object object, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        // 객체의 클래스에서 필드를 찾는다.
        Field field = object.getClass().getDeclaredField(fieldName);

        // private 필드에 접근할 수 있도록 설정한다.
        field.setAccessible(true);

        // 새로운 값으로 필드를 설정한다.
        field.set(object, newValue);
    }


    public static void changeModifyDateForce(Long id, LikeablePersonRepository repository, EntityManager em) {
        repository.changeModifyDate(id);
        em.refresh(repository.findById(id).get());
    }
}

