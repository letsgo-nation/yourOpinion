package com.example.youropinion.repository;

import com.example.youropinion.entity.OpinionB;
import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpinionBRepository extends JpaRepository<OpinionB, Long> {

    // User, Post에서 단건 조회하기
    Optional<OpinionB> findByUserAndPost(User user, Post post);
}

