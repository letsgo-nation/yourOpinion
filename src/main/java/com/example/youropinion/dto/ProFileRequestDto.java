package com.example.youropinion.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProFileRequestDto {
    private String username;
    private String email;
    private String nickname;
    @Size(min = 10, max = 1000,
          message = "최소 10자이상 최대 1000자 이하로 작성해주세요.") // 자기소개 최소 10자 이상 최대 1000자 이하
    private String introduce;
}