package com.example.youropinion.controller;

import com.example.youropinion.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/cover")
    public String cover(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println("test = " + userDetails);
        return "cover";
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println("test = " + userDetails);
        return "index";
    }
}
