package com.example.youropinion.dto;

import com.example.youropinion.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class pwChangeResponseDto {
    private String password;

    public pwChangeResponseDto(User user) {
        this.password = user.getPassword();
    }
}
