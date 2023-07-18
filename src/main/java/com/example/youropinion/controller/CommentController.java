package com.example.youropinion.controller;

import com.example.youropinion.dto.CommentRequestDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/post/{id}/comment")
    public ResponseEntity<RestApiResponseDto> createComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(id,requestDto, userDetails.getUser());
    }


    // 댓글 수정
    @PutMapping("/comment/{id}")
    public ResponseEntity<RestApiResponseDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id,requestDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<RestApiResponseDto> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id,userDetails.getUser());
    }
}
