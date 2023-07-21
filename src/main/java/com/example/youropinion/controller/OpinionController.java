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

    //OpinionA up Api
    @PostMapping("/post/detail-page/vote/{id}")
    public OpinionResponseDto OpinionAPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return opinionService.OpinionAPost(id, userDetails.getUser());
    }
}

//    // 게시글 Like 취소 API
//    @DeleteMapping("/post/like/{id}")
//    public LikeResponseDto deleteLikePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return likeService.deleteLikePost(id, userDetails.getUser());
//    }
//
//    // 댓글 Like Api
//    @PostMapping("/comment/like/{id}")
//    public LikeResponseDto likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return likeService.likeComment(id, userDetails.getUser());
//    }
//
//    // 댓글 Like 취소 API
//    @DeleteMapping("/comment/like/{id}")
//    public LikeResponseDto deleteLikeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return likeService.deleteLikeComment(id, userDetails.getUser());
//    }
//}
