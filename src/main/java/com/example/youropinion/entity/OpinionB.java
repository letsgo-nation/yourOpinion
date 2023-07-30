package com.example.youropinion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "opinion_b_Cnt") // like 테이블 생성
public class OpinionB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean opinionB = true;

    // post_id 관계도, 다대일
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // user_id 관계도, 다대일
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // OpinionB 요청 시, user, post 요청
    public OpinionB(User user, Post post) {
        setUser(user);
        setPost(post);
    }

    // setUser 메서드 생성
    public void setUser(User user) {
        this.user = user;
    }

    // setPost 메서드 생성
    public void setPost(Post post) {
        this.post = post;
    }

    //    OpinionB 여부
    public void changeOpinion() {
        if(this.opinionB){
            this.opinionB = false;
        }else {
            this.opinionB = true;
        }
    }
}
