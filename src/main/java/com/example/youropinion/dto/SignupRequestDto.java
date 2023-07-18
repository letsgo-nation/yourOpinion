package com.example.youropinion.dto;

import com.example.youropinion.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$",
            message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9) 로 구성되어야 합니다.")
    @NotBlank(message = "Id 입력이 안되었습니다.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 로 구성되어야 합니다.")
    @NotBlank(message = "비밀번호 입력이 안되었습니다.")
    private String password;

    private String introduce;

    @NotBlank(message = "닉네임 입력이 안되었습니다.")
    private String nickname;

    @Email(message = "올바른 이메일이 아닙니다.")
    private String email;

    private boolean admin = false;
    private String adminToken = "";

}
