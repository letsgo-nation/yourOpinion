package com.example.youropinion.repository;

import com.example.youropinion.entity.OpinionB;
import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface OpinionBRepository extends JpaRepository<OpinionB, Long>{

    Optional<OpinionB> findByUserAndPost(User user, Post post);
    //Collection<OpinionB> findByUser(User user);

    Collection<Object> findByUserAndOpinionB(User user, boolean opinionB);
}

