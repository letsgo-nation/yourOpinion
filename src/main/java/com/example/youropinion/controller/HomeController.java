package com.example.youropinion.controller;

import com.example.youropinion.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println("test = " + userDetails);
        return "index";
    }

    @GetMapping("/child")
    public String single() {
        return "child";
    }
}
