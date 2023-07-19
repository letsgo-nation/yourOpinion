package com.example.youropinion.controller;

import com.example.youropinion.security.UserDetailsImp;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetailsImp userDetails) {

        System.out.println("test = " + userDetails);
        return "index";
    }

    @GetMapping("/test")
    public String single() {
        return "test";
    }
}
