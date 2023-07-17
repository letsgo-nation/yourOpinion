package com.example.youropinion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProFileResponseDto {
    private String user_id;
    private String userName;
    private String password;
    private String intro;

//    public ProfileResponseDto(User user) {
//        this.user_id = user.getUserId();
//        this.userName = user.getUsername();
//        this.password = user.getPassword();
//        this.intro = user.getIntro();
//    }
}
