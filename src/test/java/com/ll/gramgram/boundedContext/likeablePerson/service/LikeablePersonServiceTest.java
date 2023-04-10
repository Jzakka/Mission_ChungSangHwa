package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LikeablePersonServiceTest {
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 호감표시_성공() {
        Member member3 = memberRepository.findByUsername("user3").get();
        Member member2 = memberRepository.findByUsername("user2").get();
        RsData<LikeablePerson> result = likeablePersonService.like(member2, member3.getInstaMember().getUsername(), 0);
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void 호감표시_성공_이유변경() {
        Member member3 = memberRepository.findByUsername("user3").get();
        Member member2 = memberRepository.findByUsername("user2").get();
        likeablePersonService.like(member2, member3.getInstaMember().getUsername(), 0);
        RsData<LikeablePerson> result = likeablePersonService.like(member2, member3.getInstaMember().getUsername(), 2);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMsg()).isEqualTo("호감이유가 바뀌었습니다.");
    }

    @Test
    void 호감표시_실패_중복데이터() {
        Member member3 = memberRepository.findByUsername("user3").get();
        Member member2 = memberRepository.findByUsername("user2").get();
        likeablePersonService.like(member2, member3.getInstaMember().getUsername(), 0);
        RsData<LikeablePerson> result = likeablePersonService.like(member2, member3.getInstaMember().getUsername(), 0);

        assertThat(result.isFail()).isTrue();
        assertThat(result.getMsg()).isEqualTo("이미 호감표시하였습니다.");
    }

    @Test
    void 호감표시_실패_없는_사람() {
        Member member2 = memberRepository.findByUsername("user2").get();
        RsData<LikeablePerson> result = likeablePersonService.like(member2, "PSEUDO_USER", 0);

        assertThat(result.isFail()).isTrue();
        assertThat(result.getMsg()).isEqualTo("사용자 인스타정보가 없습니다.");
    }
}