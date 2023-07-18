package com.example.youropinion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {
    private String nickname;

    private ProFileResponseDto proFileResponseDto;
}
