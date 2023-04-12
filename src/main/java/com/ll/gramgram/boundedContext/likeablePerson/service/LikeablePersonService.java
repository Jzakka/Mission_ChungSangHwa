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
        return RsData.produce()
                .then(rsData -> validator.checkOwnInstagramId(member, rsData))
                .then(rsData -> validator.checkSelfLike(member, username,(RsData) rsData))
                .then(rsData -> validator.checkAlreadyLike(member, username, attractiveTypeCode, (RsData) rsData))
                .then(rsData -> validator.checkMaximumLike(member, (RsData) rsData))
                .then(rsData -> successfulLike(member, username, attractiveTypeCode, (RsData) rsData));
    }

    private RsData<LikeablePerson> successfulLike(Member member, String username, int attractiveTypeCode, RsData rsData) {
        InstaMember toInstaMember = (InstaMember) rsData.getAttribute("toInstaMember");

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(member.getInstaMember()) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    @Transactional
    public void deletePairByPairId(long id) {
        likeablePersonRepository.deleteById(id);
    }

    public RsData<LikeablePerson> getLikee(long id) {
        Optional<LikeablePerson> pair = likeablePersonRepository.findById(id);
        return pair.map(RsData::successOf).orElseGet(() -> RsData.of("F-1", "존재하지 않는 페어입니다."));
    }
}
