package com.example.youropinion.controller;

import com.example.youropinion.dto.CommentResponseDto;
import com.example.youropinion.dto.PostResponseDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.dto.admin.AdminUserResponseDto;
import com.example.youropinion.dto.admin.RoleChangeRequestDto;
import com.example.youropinion.entity.UserRoleEnum;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;


    // 회원 정보 가져오기
    @GetMapping("/user")
    public List<AdminUserResponseDto> getAdminPageUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return adminService.getAdminPageUsers();
        } else {
            throw new SecurityException("관리자가 아닌 사용자는 접근 불가");
        }
    }


    // 회원 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<RestApiResponseDto> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return adminService.deleteUser(id);
        } else {
            throw new SecurityException("관리자가 아닌 사용자는 접근 불가");
        }
    }


    // 회원 권한 수정
    @PutMapping("/users/{id}")
    public ResponseEntity<RestApiResponseDto> changeRoleUser(
            @PathVariable Long id, @RequestBody RoleChangeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(String.valueOf(userDetails.getUser().getRole()));
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return adminService.changeRoleUser(id, requestDto);
        } else {
            throw new SecurityException("관리자가 아닌 사용자는 접근 불가");
        }
    }


    // 게시글 정보 가져오기
    @GetMapping("/post")
    public List<PostResponseDto> getAdminPostPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return adminService.getAdminPagePosts();
        } else {
            throw new SecurityException("관리자가 아닌 사용자는 접근 불가");
        }
    }


    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<RestApiResponseDto> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return adminService.deleteAdminPagePost(id);
        } else {
            throw new SecurityException("관리자가 아닌 사용자는 접근 불가");
        }
    }


    // 댓글 정보 가져오기
    @GetMapping("/comment")
    public List<CommentResponseDto> getAdminCommentPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return adminService.getAdminPageComments();
        } else {
            throw new SecurityException("관리자가 아닌 사용자는 접근 불가");
        }
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<RestApiResponseDto> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return adminService.deleteAdminPageComment(id);
        } else {
            throw new SecurityException("관리자가 아닌 사용자는 접근 불가");
        }
    }

}
