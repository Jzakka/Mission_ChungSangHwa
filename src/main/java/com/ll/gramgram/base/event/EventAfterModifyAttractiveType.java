package com.ll.gramgram.base.event;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventAfterModifyAttractiveType extends ApplicationEvent {
    private final LikeablePerson likeablePerson;
    private final Integer oldAttractiveTypeCode;
    public EventAfterModifyAttractiveType(Object source, LikeablePerson likeablePerson, Integer oldAttractiveTypeCode) {
        super(source);
        this.likeablePerson = likeablePerson;
        this.oldAttractiveTypeCode = oldAttractiveTypeCode;
    }
}
