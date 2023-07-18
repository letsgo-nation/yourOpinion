package com.example.youropinion.dto;

import com.example.youropinion.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String username;
    private String opinionA;
    private String opinionB;
    private Long opinionACnt;
    private Long opinionBCnt;
    private LocalDateTime modifiedAt;
    //private List<Comment> commentList;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.opinionA = post.getOpinionA();
        this.opinionB = post.getOpinionB();
        this.opinionACnt = post.getOpinionACnt();
        this.opinionBCnt = post.getOpinionBCnt();
        this.modifiedAt = post.getModifiedAt();
/*        this.commentResponseDtoList = new ArrayList<>();
        if(post.getCommentList().size()>0) {
            for (Comment comment : post.getCommentList()) {
                this.commentResponseDtoList.add(new CommentResponseDto(comment));
            }
        }// end of the if()*/
    }// end of constructor method()
}
