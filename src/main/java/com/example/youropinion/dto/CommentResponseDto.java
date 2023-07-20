package com.example.youropinion.dto;

import com.example.youropinion.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private Long likeCnt;
    private Long dislikeCnt;
    private Long post_id;
    private Long user_id;
    private String username;
    private String nickname;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likeCnt = comment.getLikeCnt();
        this.dislikeCnt = comment.getDislikeCnt();
        this.post_id = comment.getPost().getId();
        this.user_id = comment.getUser().getId();
        this.username = comment.getUser().getUsername();
        this.nickname = comment.getUser().getNickname();
        this.modifiedAt = comment.getModifiedAt();
    }

}
