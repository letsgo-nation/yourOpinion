package com.example.youropinion.repository;

import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Long> {

    Collection<Post> findByUser(User user);
}
