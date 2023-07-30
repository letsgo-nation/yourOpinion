package com.example.youropinion.entity;

import com.example.youropinion.dto.SecondCommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@Entity
@DynamicInsert
@NoArgsConstructor
@Table(name = "secondComments")
public class  SecondComments extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ColumnDefault("0")
    private Long likeCnt;

    @ColumnDefault("0")
    private Long dislikeCnt;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SecondComments(String second, Comment comment, User user) {
        this.content = second;
        this.likeCnt = Long.getLong("0");
        this.dislikeCnt = Long.getLong("0");
        this.comment = comment;
        this.user = user;
    }

    public void update(SecondCommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}