package com.example.youropinion.entity;

import com.example.youropinion.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name = "posts")
@NoArgsConstructor
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Column(name = "opiniona")
    private String opinionA;

    @Column(name = "opinionb")
    private String opinionB;

    @ColumnDefault("0")
    @Column(name = "opinion_a_Cnt")
    private Long opinionACnt;

    @ColumnDefault("0")
    @Column(name = "opinionb_Cnt")
    private Long opinionBCnt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @OneToMany( mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.opinionA = requestDto.getOpinionA();
        this.opinionB = requestDto.getOpinionB();
        // 첫 게시글 등록시 투표값 모두 0임.
        this.opinionACnt = Long.getLong("0");
        this.opinionBCnt = Long.getLong("0");
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.opinionA = requestDto.getOpinionA();
        this.opinionB = requestDto.getOpinionB();
    }

}
