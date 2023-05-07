package com.ll.gramgram.boundedContext.likeablePerson.validator;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.instaMember.entity.InstaMember;
import com.ll.gramgram.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeablePersonValidator {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Value("${constant.max-likeable-person}")
    private Integer maxLikeablePerson;

    @Value("${constant.modify-delete-cooltime}")
    private Integer coolTime;

    public RsData<LikeablePerson> checkOwnInstagramId(Member member, RsData<LikeablePerson> rsData) {
        if (member.hasConnectedInstaMember() == false) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }
        return rsData;
    }

    public RsData<LikeablePerson> checkSelfLike(Member member, String username, RsData<LikeablePerson> rsData) {
        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }
        return rsData;
    }

    public RsData<LikeablePerson> checkAlreadyLike(Member member, String username,
                                                   int attractiveTypeCode, RsData<LikeablePerson> rsData) {
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();
        Optional<LikeablePerson> likeInfoOptional = likeablePersonRepository
                .findByFromInstaMemberIdAndToInstaMemberId(member.getInstaMember().getId(), toInstaMember.getId());

        if (likeInfoOptional.isPresent()) {
            LikeablePerson likeablePerson = likeInfoOptional.get();
            return checkSameReason(attractiveTypeCode, likeablePerson, rsData);
        }

        rsData.setAttribute("toInstaMember", toInstaMember);
        return rsData;
    }

    private RsData<LikeablePerson> checkSameReason(int attractiveTypeCode, LikeablePerson likeablePerson, RsData<LikeablePerson> rsData) {
        if (likeablePerson.getAttractiveTypeCode() == attractiveTypeCode) {
            return RsData.of("F-3", "이미 호감표시하였습니다.");
        }
        rsData.setAttribute("likeablePerson", likeablePerson);
        rsData.setResultCode(RsData.EXCEPTION);
        return rsData;
    }

    public RsData<LikeablePerson> checkMaximumLike(Member member, RsData<LikeablePerson> rsData) {
        List<LikeablePerson> likeList = likeablePersonRepository.findByFromInstaMemberId(member.getInstaMember().getId());
        if (likeList.size() == maxLikeablePerson) {
            return RsData.of("F-4", "호감상대는 최대 %d명까지 등록 가능합니다.".formatted(maxLikeablePerson));
        }
        return rsData;
    }

    public RsData<LikeablePerson> checkCoolTime(String message, RsData<LikeablePerson> rsData) {
        LikeablePerson likeablePerson = (LikeablePerson) rsData.getAttribute("likeablePerson");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime modifyDate = likeablePerson.getModifyDate();
        long minutesBetween = ChronoUnit.MINUTES.between(modifyDate, now);

        if (minutesBetween < coolTime) {
            return RsData.of("F-5", message.formatted(coolTime));
        }
        return rsData;
    }
}
