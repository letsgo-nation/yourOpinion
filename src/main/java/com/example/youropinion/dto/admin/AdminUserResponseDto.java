package com.example.youropinion.dto.admin;


import com.example.youropinion.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserResponseDto {

    private Long id; // user 테이블의 pk값
    private String username;
    private String nickname;
    private String email;
    private String role;
    // 투표 참여 수
    private int voteCnt;
    // 작성 게시글 수와 목록
    private int postCnt;
    // 작성 댓글 수와 목록
    private int commentCnt;

    public AdminUserResponseDto(
            User user, int voteCnt, int postCnt, int commentCnt) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.role = String.valueOf(user.getRole());
        this.voteCnt = voteCnt;
        this.postCnt = postCnt;
        this.commentCnt = commentCnt;
    }
}
