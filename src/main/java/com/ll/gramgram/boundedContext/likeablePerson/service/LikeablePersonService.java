package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
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
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        return RsData
                .produce(() -> checkOwnInstagramId(member))
                .then(rsData -> checkSelfLike(member, username, rsData))
                .then(rsData -> checkAlreadyLike(member, username, attractiveTypeCode, (RsData) rsData))
                .then(rsData -> checkMaximumLike(member, (RsData) rsData))
                .then(rsData -> successfulLike(member, username, attractiveTypeCode, (RsData) rsData));
    }

    private RsData<LikeablePerson> checkOwnInstagramId(Member member) {
        if (member.hasConnectedInstaMember() == false) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }
        return RsData.of("P", "Not Completed");
    }

    private RsData<LikeablePerson> checkSelfLike(Member member, String username, RsData rsData) {
        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }
        return rsData;
    }

    private RsData<LikeablePerson> checkAlreadyLike(Member member, String username, int attractiveTypeCode, RsData rsData) {
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();
        Optional<LikeablePerson> likeInfoOptional = likeablePersonRepository
                .findByFromInstaMemberIdAndToInstaMemberId(member.getInstaMember().getId(), toInstaMember.getId());

        if (likeInfoOptional.isPresent()) {
            LikeablePerson likeablePerson = likeInfoOptional.get();
            return changeReasonOrNot(attractiveTypeCode, likeablePerson);
        }

        rsData.setAttribute("toInstaMember", toInstaMember);
        return rsData;
    }

    private RsData<LikeablePerson> changeReasonOrNot(int attractiveTypeCode, LikeablePerson likeablePerson) {
        if (likeablePerson.getAttractiveTypeCode() == attractiveTypeCode) {
            return RsData.of("F-3", "이미 호감표시하였습니다.");
        }
        likeablePerson.changeAttractiveType(attractiveTypeCode);
        likeablePersonRepository.save(likeablePerson);
        return RsData.of("S-2", "호감이유가 바뀌었습니다.", likeablePerson);
    }

    private RsData<LikeablePerson> checkMaximumLike(Member member, RsData rsData) {
        List<LikeablePerson> likeList = likeablePersonRepository.findByFromInstaMemberId(member.getInstaMember().getId());
        if (likeList.size() == 10) {
            return RsData.of("F-4", "호감상대는 최대 10명까지 등록 가능합니다.");
        }
        return rsData;
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
