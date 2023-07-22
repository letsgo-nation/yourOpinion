package com.example.youropinion.controller;

import com.example.youropinion.dto.PostResponseDto;
import com.example.youropinion.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostViewController {

    private final PostService postService;

    @GetMapping("/post/write-page")
    public String writePage(){
        return "postWrite";
    }


    // 게시글 페이지 보여주기
    @GetMapping("/post/detail-page/{id}")
    public String bringPost(@PathVariable Long id,
                            Model model) {
        PostResponseDto result = postService.getPost(id);
        model.addAttribute("post", result);
//        model.addAttribute("commentList", responseDto.getCommentResponseDtoList());

        return "postDetail";
    }

    // 게시글 수정 페이지 보여주기
    @GetMapping("/post/modify-page/{id}")
    public String modifyPost(Model model,  @PathVariable Long id)
            throws JsonProcessingException {
        model.addAttribute("info_post",postService.getPost(id));
        return "postModify";
    }
}
