package com.example.youropinion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecondCommentRequestDto {
    @NotBlank(message = "내용이 입력이 안되었습니다.")
    private String content;
}