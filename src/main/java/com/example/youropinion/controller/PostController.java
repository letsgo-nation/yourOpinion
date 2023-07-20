package com.example.youropinion.controller;

import com.example.youropinion.dto.PostRequestDto;
import com.example.youropinion.dto.PostResponseDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.exception.TokenNotValidateException;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.PostService;
import jakarta.validation.Valid;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;


    @GetMapping("/post/write-page")
    public String writePage(){
        return "postWrite";
    }

    // 전체 게시글 조회
    @GetMapping("/posts")
    public @ResponseBody ResponseEntity<RestApiResponseDto> getPostList() {
        return postService.getPostList();
    }

    // 선택 게시글 조회
/*    @GetMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> getPost(
            @PathVariable Long id) {
        return postService.getPost(id);
    }*/

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

    // 게시글 작성
    @PostMapping("/post")
    public @ResponseBody ResponseEntity<RestApiResponseDto> createPost(
            @Valid @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return postService.createPost(requestDto,userDetails.getUser());

    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return postService.updatePost(id, requestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return postService.deletePost(id, userDetails.getUser());
    }

    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (Exception ex){
            throw new TokenNotValidateException("토큰이 유효하지 않습니다.");
        }
    }

}
