package com.example.youropinion.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRequestDto {

    private String title;
    private String content;
    private String opinionA;
    private String opinionB;

}
