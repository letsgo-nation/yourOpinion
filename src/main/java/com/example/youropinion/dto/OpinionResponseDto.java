package com.example.youropinion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor // 생성자를 자동으로 생성
@ToString // 필드 값을 String으로 변환 , toString() 메서드 자동 생성

public class OpinionResponseDto {
    private String msg; // Client에게 보내는 메시지
    private int code; // 상태코드 반환

    @Builder
    public OpinionResponseDto(String msg, int code){
        this.msg = msg;
        this.code = code;
    }
}
