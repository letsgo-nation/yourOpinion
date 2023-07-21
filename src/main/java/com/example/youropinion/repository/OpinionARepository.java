package com.example.youropinion.repository;

import com.example.youropinion.entity.OpinionA;
import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpinionARepository extends JpaRepository<OpinionA, Long> {

    // User, Post에서 단건 조회하기
     Optional<OpinionA> findByUserAndPost(User user, Post post);
//    Optional<OpinionB> findByUserAndPost(User user, Post post);
}

