package com.example.youropinion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRequestDto {

    @NotBlank(message = "내용이 입력이 안되었습니다.")
    private String content;
}
