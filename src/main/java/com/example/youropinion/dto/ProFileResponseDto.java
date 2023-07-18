package com.example.youropinion.dto;

import com.example.youropinion.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProFileResponseDto {
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String introduce;


    public ProFileResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
    }
}
