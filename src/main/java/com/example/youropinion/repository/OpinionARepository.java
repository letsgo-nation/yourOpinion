package com.example.youropinion.repository;

import com.example.youropinion.entity.OpinionA;
import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OpinionARepository extends JpaRepository<OpinionA, Long> {

    Optional<OpinionA> findByUserAndPost(User user, Post post);

    Collection<Object> findByUserAndOpinionA(User user, boolean opinionA);

    List<OpinionA> findByUser(User user);

    List<OpinionA> findByPost(Post post);
}

