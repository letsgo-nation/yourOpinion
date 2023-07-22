package com.example.youropinion.controller;

import com.example.youropinion.dto.ApiResponseDto;
import com.example.youropinion.dto.SignupRequestDto;
import com.example.youropinion.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signUp(@Valid @RequestBody SignupRequestDto requestDto) {
        // @Valid를 통해 받아오는 테이터의 제한을 걸어둠
        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(200).body(new ApiResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

}