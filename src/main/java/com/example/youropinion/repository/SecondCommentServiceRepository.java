package com.example.youropinion.repository;

import com.example.youropinion.entity.SecondComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecondCommentServiceRepository extends JpaRepository<SecondComments,Long> {

    List<SecondComments> findAllByCommentIdOrderByCreatedAtAsc(Long id);

}
