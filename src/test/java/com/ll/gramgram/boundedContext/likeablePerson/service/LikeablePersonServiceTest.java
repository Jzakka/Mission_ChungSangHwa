package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LikeablePersonServiceTest {
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${constant.max-likeable-person}")
    private Integer maxLikeablePerson;

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
    void 호감표시_실패_한도초과() {
        Member member2 = memberRepository.findByUsername("user2").get();
        for (int i = 0; i < 10; i++) {
            likeablePersonService.like(member2, "dummy_insta_mem_%s".formatted(i), 0);
        }

        RsData<LikeablePerson> result = likeablePersonService.like(member2, "exceed_likeable_person", 1);

        assertThat(result.isFail()).isTrue();
        assertThat(result.getMsg()).isEqualTo("호감상대는 최대 %d명까지 등록 가능합니다.".formatted(maxLikeablePerson));
    }
}