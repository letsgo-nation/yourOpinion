package com.example.youropinion.dto;

import com.example.youropinion.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long id;
    private String content;
    private Long likeCnt;
    private Long dislikeCnt;
    private Long post_id;
    private Long user_id;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likeCnt = comment.getLikeCnt();
        this.dislikeCnt = comment.getDislikeCnt();
        this.post_id = comment.getPost().getId();
        this.user_id = comment.getUser().getId();
    }

}
