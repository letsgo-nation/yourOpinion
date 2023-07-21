package com.example.youropinion.controller;

import com.example.youropinion.dto.OpinionResponseDto;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.OpinionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpinionController {

    private final OpinionService opinionService;

    @PostMapping("/posts/{id}/opinionA") // 해당 게시글의 옵션A 증가
    public OpinionResponseDto increaseOpinionA(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return opinionService.increaseOpinionA(id, userDetails.getUser());
    }

    @PostMapping("/posts/{id}/opinionB") // 해당 게시글의 옵션B 증가
    public OpinionResponseDto increaseOpinionB(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return opinionService.increaseOpinionB(id, userDetails.getUser());
    }
    @PutMapping("/posts/{id}/opinionA") // 해당 게시글의 옵션A 감소
    public OpinionResponseDto decreaseOpinionA(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return opinionService.decreaseOpinionA(id, userDetails.getUser());
    }
    @PutMapping("/posts/{id}/opinionB") // 해당 게시글의 옵션B 감소
    public OpinionResponseDto decreaseOpinionB(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return opinionService.decreaseOpinionB(id, userDetails.getUser());
    }
}
