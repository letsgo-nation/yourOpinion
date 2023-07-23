package com.example.youropinion.controller;

import com.example.youropinion.dto.CommentResponseDto;
import com.example.youropinion.dto.PostResponseDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.dto.admin.AdminUserResponseDto;
import com.example.youropinion.dto.admin.RoleChangeRequestDto;
import com.example.youropinion.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    // 회원 정보 가져오기
    @GetMapping("/user")
    public List<AdminUserResponseDto> getAdminPageUsers(){
        return adminService.getAdminPageUsers();
    }


    // 회원 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<RestApiResponseDto> deleteUser(
            @PathVariable Long id) {
        return adminService.deleteUser(id);
    }


    // 회원 권한 수정
    @PutMapping("/users/{id}")
    public ResponseEntity<RestApiResponseDto> changeRoleUser(
            @PathVariable Long id, @RequestBody RoleChangeRequestDto requestDto) {
        return adminService.changeRoleUser(id,requestDto);
    }


    // 게시글 정보 가져오기
    @GetMapping("/post")
    public List<PostResponseDto> getAdminPostPage() {
        return adminService.getAdminPagePosts();
    }


    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<RestApiResponseDto> deletePost(
            @PathVariable Long id) {
        return adminService.deleteAdminPagePost(id);
    }


    // 댓글 정보 가져오기
    @GetMapping("/comment")
    public List<CommentResponseDto> getAdminCommentPage() {
       return adminService.getAdminPageComments();
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<RestApiResponseDto> deleteComment(
            @PathVariable Long id) {
        return adminService.deleteAdminPageComment(id);
    }

}
