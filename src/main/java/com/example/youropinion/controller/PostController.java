package com.example.youropinion.controller;

import com.example.youropinion.dto.PostRequestDto;
import com.example.youropinion.dto.PostResponseDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.exception.TokenNotValidateException;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.PostService;
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
    @GetMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> getPost(
            @PathVariable Long id) {
        return postService.getPost(id);
    }

    // 투표 상세 게시물
    @GetMapping("/post/detail-page")
    public String singlePage() {
        return "single-page";
    }

    @GetMapping("/vs-page")
    public String vsPage() {
        return "vs_voting_system";
    }
//    @GetMapping("/vote-page/{id}")
//    @ResponseBody
//    public String getPost(@PathVariable Long id,
//                          Model model) {
//        model.addAttribute("post", postService.getPost(id));
//
//        return "vote"; // vote.html view
//    }
//@GetMapping("/profile")
//public String getPost(Model model,
//                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
//    model.addAttribute("user", userDetails.getUser());
//    return "profile"; // profile.html view
//}


    // 게시글 작성
    @PostMapping("/post")
    public @ResponseBody ResponseEntity<RestApiResponseDto> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return postService.createPost(requestDto,userDetails.getUser());

    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<RestApiResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto,
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
