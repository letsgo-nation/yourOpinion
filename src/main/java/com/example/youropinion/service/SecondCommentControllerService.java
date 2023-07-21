package com.example.youropinion.service;


import com.example.youropinion.repository.SecondCommentControllerServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecondCommentControllerService {

    private final SecondCommentControllerServiceRepository secondCommentControllerServiceRepository;

}
