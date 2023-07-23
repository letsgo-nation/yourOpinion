package com.example.youropinion.repository;

import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);
}
