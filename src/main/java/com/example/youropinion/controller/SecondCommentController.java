package com.example.youropinion.controller;

import com.example.youropinion.service.SecondCommentControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class SecondCommentController {

    private final SecondCommentControllerService secondCommentControllerService;

//    @PostMapping("/comment/{id}/scondComment")
//    public void scondComment

}
