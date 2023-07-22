package com.example.youropinion.controller;

import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.dto.SecondCommentRequestDto;
import com.example.youropinion.exception.TokenNotValidateException;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.SecondCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class SecondCommentController {

    private final SecondCommentService secondCommentService;

    @PostMapping("/comment/{id}/secondComment")
    public ResponseEntity<RestApiResponseDto> createSecondComment(@PathVariable Long id,
                                                                  @Valid @RequestBody SecondCommentRequestDto requestDto,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        this.tokenValidate(userDetails);
        return secondCommentService.createSecondComment(id, requestDto, userDetails.getUser());
    }

    @PutMapping("/secondComments/{id}")
    public ResponseEntity<RestApiResponseDto> updateComment(
            @PathVariable Long id,
            @Valid  @RequestBody SecondCommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return secondCommentService.updateComment(id,requestDto, userDetails.getUser());
    }

    @DeleteMapping("/secondComments/{id}")
    public ResponseEntity<RestApiResponseDto> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        this.tokenValidate(userDetails);
        return secondCommentService.deleteComment(id,userDetails.getUser());
    }

    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        }catch (Exception ex){
            throw new TokenNotValidateException("토큰이 유효하지 않습니다.");
        }
    }
}