package com.example.youropinion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "opinion_a_Cnt") // like 테이블 생성
public class OpinionA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean opinionA = true;

    // post_id 관계도, 다대일
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // user_id 관계도, 다대일
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // OpinionA 요청 시, user, post 요청
    public OpinionA(User user, Post post) {
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

//    OpinionA 여부
    public void changeOpinion() {
        if(this.opinionA){
            this.opinionA = false;
        }else {
            this.opinionA = true;
        }
    }

}
