package com.example.youropinion.repository;

import com.example.youropinion.entity.SecondComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondCommentControllerServiceRepository extends JpaRepository<SecondComments,Long> {

}
