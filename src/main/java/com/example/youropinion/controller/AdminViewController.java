package com.example.youropinion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/admin")
public class AdminViewController {

    @GetMapping("/user-page")
    public String userPageLoad(Model model){
        return "admin-page/adminUserPage";
    }

    @GetMapping("/post-page")
    public String getAdminPostPage(Model model) {
        return "admin-page/adminPostPage";
    }

    @GetMapping("/comment-page")
    public String getAdminCommentPage(Model model) {
        return "admin-page/adminCommentPage";
    }

}
