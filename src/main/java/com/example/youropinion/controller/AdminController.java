package com.example.youropinion.controller;

import com.example.youropinion.dto.RestApiResponseDto;

import com.example.youropinion.dto.admin.AdminUserResponseDto;
import com.example.youropinion.dto.admin.RoleChangeRequestDto;
import com.example.youropinion.service.CommentService;
import com.example.youropinion.service.PostService;
import com.example.youropinion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;


    // 회원 정보 가져오기
    @GetMapping("/user")
    public List<AdminUserResponseDto> userPageLoad(){
        return userService.getAdminPageUsers();
    }


    // 회원 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<RestApiResponseDto> deleteUser(
            @PathVariable Long id) {
        return userService.deleteUser(id);
    }


    // 회원 권한 수정
    @PutMapping("/users/{id}")
    public ResponseEntity<RestApiResponseDto> changeRoleUser(
            @PathVariable Long id, @RequestBody RoleChangeRequestDto requestDto) {
        return userService.changeRoleUser(id,requestDto);
    }


//    // 게시글 정보 가져오기
//    @GetMapping("/post")
//    public List<AdminPostResponseDto> getAdminPostPage(Model model) {
//        return postService.getAdminPagePosts();
//    }
//
//    // 댓글 정보 가져오기
//    @GetMapping("/comment")
//    public List<AdminCommentResponseDto> getAdminCommentPage(Model model) {
//       return commentService.getAdminPageComments();
//    }
//
}
