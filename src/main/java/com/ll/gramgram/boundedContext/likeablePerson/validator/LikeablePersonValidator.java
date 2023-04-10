package com.ll.gramgram.boundedContext.likeablePerson.validator;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeablePersonValidator {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    public RsData<LikeablePerson> checkOwnInstagramId(Member member) {
        if (member.hasConnectedInstaMember() == false) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }
        return RsData.of("P", "Not Completed");
    }

    public RsData<LikeablePerson> checkSelfLike(Member member, String username, RsData rsData) {
        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }
        return rsData;
    }

    public RsData<LikeablePerson> checkAlreadyLike(Member member, String username, int attractiveTypeCode, RsData rsData) {
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

    public RsData<LikeablePerson> changeReasonOrNot(int attractiveTypeCode, LikeablePerson likeablePerson) {
        if (likeablePerson.getAttractiveTypeCode() == attractiveTypeCode) {
            return RsData.of("F-3", "이미 호감표시하였습니다.");
        }
        likeablePerson.changeAttractiveType(attractiveTypeCode);
        likeablePersonRepository.save(likeablePerson);
        return RsData.of("S-2", "호감이유가 바뀌었습니다.", likeablePerson);
    }

    public RsData<LikeablePerson> checkMaximumLike(Member member, RsData rsData) {
        List<LikeablePerson> likeList = likeablePersonRepository.findByFromInstaMemberId(member.getInstaMember().getId());
        if (likeList.size() == 10) {
            return RsData.of("F-4", "호감상대는 최대 10명까지 등록 가능합니다.");
        }
        return rsData;
    }
}
