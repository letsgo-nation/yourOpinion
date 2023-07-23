package com.example.youropinion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class UserViewController {

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    // 프로필 확인
    @GetMapping("/profile-page")
    public String getProfilePage() {
        return "profile";
    }

    //비밀번호 변경
    @GetMapping("/profile-page/pwchange")
    public String getPwChangePage() {
        return "pwchange";
    }

    @GetMapping("/admin-page/user")
    public String getAdminUserPage() {
        return "adminUserPage";
    }

    @GetMapping("/admin-page/post")
    public String getAdminPostPage() {
        return "adminPostPage";
    }

    @GetMapping("/admin-page/comment")
    public String getAdminCommenttPage() {
        return "adminCommentPage";
    }

}
