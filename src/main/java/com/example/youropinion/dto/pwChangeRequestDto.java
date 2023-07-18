package com.example.youropinion.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class pwChangeRequestDto {
    private String username;
    private String password;
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 7  자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성")
    private String checkPassword;
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 7  자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성")
    private String newPassword;
}
