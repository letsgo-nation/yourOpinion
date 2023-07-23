package com.example.youropinion.controller;

import com.example.youropinion.dto.admin.AdminCommentResponseDto;
import com.example.youropinion.dto.admin.AdminPostResponseDto;
import com.example.youropinion.dto.admin.AdminUserResponseDto;
import com.example.youropinion.service.CommentService;
import com.example.youropinion.service.PostService;
import com.example.youropinion.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminViewController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/user-page")
    public String userPageLoad(Model model){
/*        List<AdminUserResponseDto> result = userService.getAdminPageUsers();
        log.info(result.toString());
        model.addAttribute("userList", result);*/
        return "admin-page/adminUserPage";
    }

    @GetMapping("/post-page")
    public String getAdminPostPage(Model model) {
/*        List<AdminPostResponseDto> result = postService.getAdminPagePosts();
        log.info(result.toString());
        model.addAttribute("postList", result);*/
        return "admin-page/adminPostPage";
    }

    @GetMapping("/comment-page")
    public String getAdminCommentPage(Model model) {
/*        List<AdminCommentResponseDto> result = commentService.getAdminPageComments();
        log.info(result.toString());
        model.addAttribute("commentList", result);*/
        return "admin-page/adminCommentPage";
    }

}
