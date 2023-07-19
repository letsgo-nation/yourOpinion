package com.example.youropinion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProFileRequestDto {
    private String username;
    @Email(message = "올바른 이메일이 아닙니다.")
    private String email;
    @NotBlank(message = "닉네임 입력이 안되었습니다.")
    private String nickname;
    @Size(min = 10, max = 1000,
          message = "최소 10자이상 최대 1000자 이하로 작성해주세요.") // 자기소개 최소 10자 이상 최대 1000자 이하
    private String introduce;
}