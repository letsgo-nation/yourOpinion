package com.example.youropinion.controller;

import com.example.youropinion.dto.admin.AdminCommentResponseDto;
import com.example.youropinion.dto.admin.AdminPostResponseDto;
import com.example.youropinion.dto.admin.AdminUserResponseDto;
import com.example.youropinion.service.CommentService;
import com.example.youropinion.service.PostService;
import com.example.youropinion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/user")
    public List<AdminUserResponseDto> userPageLoad(Model model){
        return userService.getAdminPageUsers();
    }

    @GetMapping("/post")
    public List<AdminPostResponseDto> getAdminPostPage(Model model) {
        return postService.getAdminPagePosts();
    }

    @GetMapping("/comment")
    public List<AdminCommentResponseDto> getAdminCommentPage(Model model) {
       return commentService.getAdminPageComments();
    }
}
