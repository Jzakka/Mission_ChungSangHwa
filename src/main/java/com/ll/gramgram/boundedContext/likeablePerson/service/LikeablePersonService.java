package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.likeablePerson.validator.LikeablePersonValidator;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final LikeablePersonValidator validator;
    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        return RsData.produce(LikeablePerson.class)
                .then(rsData -> validator.checkOwnInstagramId(member, rsData))
                .then(rsData -> validator.checkSelfLike(member, username, rsData))
                .then(rsData -> validator.checkAlreadyLike(member, username, attractiveTypeCode, rsData))
                .then(rsData -> validator.checkMaximumLike(member, rsData))
                .then(rsData -> successfulLike(member, username, attractiveTypeCode, rsData))
                .catchEx(rsData -> changeReason(attractiveTypeCode, rsData));
    }


    private RsData<LikeablePerson> changeReason(int attractiveTypeCode, RsData<LikeablePerson> rsData) {
        LikeablePerson likeablePerson = (LikeablePerson) rsData.getAttribute("likeablePerson");

        likeablePerson.setAttractiveTypeCode(attractiveTypeCode);
        likeablePersonRepository.save(likeablePerson);

        return RsData.of("S-2", "호감이유가 바뀌었습니다.", likeablePerson);
    }

    private RsData<LikeablePerson> successfulLike(Member member, String username,
                                                  int attractiveTypeCode, RsData<LikeablePerson> rsData) {
        InstaMember toInstaMember = (InstaMember) rsData.getAttribute("toInstaMember");
        InstaMember fromInstaMember = member.getInstaMember();

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(fromInstaMember.getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        fromInstaMember.addFromLikeablePerson(likeablePerson);
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    @Transactional
    public RsData delete(LikeablePerson likeablePerson) {
        likeablePerson.getFromInstaMember().removeFromLikeablePerson(likeablePerson);
        likeablePerson.getToInstaMember().removeToLikeablePerson(likeablePerson);
        likeablePersonRepository.delete(likeablePerson);

        String likeeName = likeablePerson.getToInstaMember().getUsername();
        return RsData.of("S-1", "%s님에 대한 호감을 취소하였습니다.".formatted(likeeName));
    }

    public RsData<LikeablePerson> getLikee(long id) {
        Optional<LikeablePerson> pair = likeablePersonRepository.findById(id);
        return pair.map(RsData::successOf).orElseGet(() -> RsData.of("F-1", "존재하지 않는 페어입니다."));
    }

    public Optional<LikeablePerson> findById(long id) {
        return likeablePersonRepository.findById(id);
    }

    public Optional<LikeablePerson> findByFromInstaMember_usernameAndToInstaMember_username(String fromName, String toName) {
        return likeablePersonRepository.findByFromInstaMember_UsernameAndToInstaMember_Username(fromName,toName);
    }
}
