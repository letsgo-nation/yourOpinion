package com.example.youropinion.dto;

import com.example.youropinion.entity.Comment;
import com.example.youropinion.entity.SecondComments;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SecondCommentResponseDto {
    private Long id;
    private String content;
    private Long likeCnt;
    private Long dislikeCnt;
    private Long comment_id;
    private Long user_id;
    private String username;
    private String nickname;
    private LocalDateTime modifiedAt;

    public SecondCommentResponseDto(SecondComments comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likeCnt = comment.getLikeCnt();
        this.dislikeCnt = comment.getDislikeCnt();
        this.comment_id = comment.getComment().getId();
        this.user_id = comment.getUser().getId();
        this.username = comment.getUser().getUsername();
        this.nickname = comment.getUser().getNickname();
        this.modifiedAt = comment.getModifiedAt();
    }
}
