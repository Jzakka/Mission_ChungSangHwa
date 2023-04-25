package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);

    Optional<LikeablePerson> findByFromInstaMemberIdAndToInstaMemberId(Long fromId, Long toId);

    List<LikeablePerson> findByToInstaMember_Username(String username);

    Optional<LikeablePerson> findByFromInstaMemberIdAndToInstaMember_Username(Long id, String username);

    Optional<LikeablePerson> findByFromInstaMember_UsernameAndToInstaMember_Username(String fromUsername, String toUsername);

    @Modifying
    @Query("update LikeablePerson lp " +
            "set lp.attractiveTypeCode = :attractiveTypeCode, " +
            "lp.modifyDate = CURRENT_TIMESTAMP " +
            "where lp.id=:id")
    void updateAttractiveTypeCode(@Param("id") Long id, @Param("attractiveTypeCode") Integer typeCode);
}
