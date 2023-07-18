package com.example.youropinion.dto;

import com.example.youropinion.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProFileResponseDto {
    private String nickname;
    private String email;
    private String introduce;


    public ProFileResponseDto(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
    }
}
