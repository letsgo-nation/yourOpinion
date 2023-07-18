package com.example.youropinion.controller;

import com.example.youropinion.dto.PostRequestDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 전체 게시글 조회
    @GetMapping("/posts")
    public @ResponseBody ResponseEntity<RestApiResponseDto> getPostList() {
        return postService.getPostList();
    }


    // 선택 게시글 조회
    @GetMapping("/post/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> getPost(
            @PathVariable Long id) {
        return postService.getPost(id);
    }


    // 게시글 작성
    @PostMapping("/post")
    public @ResponseBody ResponseEntity<RestApiResponseDto> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto,userDetails.getUser());

    }

    // 게시글 수정
    @PutMapping("/post/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, userDetails.getUser());
    }


    // 게시글 삭제
    @DeleteMapping("/post/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }

}
