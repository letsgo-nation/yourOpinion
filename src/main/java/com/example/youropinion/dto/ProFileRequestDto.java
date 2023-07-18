package com.example.youropinion.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProFileRequestDto {
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 7  자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성")
    private String password;
    private String email;
    private String nickname;
    @Size(min = 10, max = 1000,
          message = "최소 10자이상 최대 1000자 이하로 작성해주세요.") // 자기소개 최소 10자 이상 최대 1000자 이하
    private String introduce;
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 7  자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성")
    private String checkPassword;
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 7  자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성")
    private String newPassword;
}