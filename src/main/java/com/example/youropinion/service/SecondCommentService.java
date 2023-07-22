package com.example.youropinion.service;


import com.example.youropinion.dto.*;
import com.example.youropinion.entity.Comment;
import com.example.youropinion.entity.SecondComments;
import com.example.youropinion.entity.User;
import com.example.youropinion.entity.UserRoleEnum;
import com.example.youropinion.exception.CommentNotFoundException;
import com.example.youropinion.exception.PostNotFoundException;
import com.example.youropinion.repository.CommentRepository;
import com.example.youropinion.repository.SecondCommentServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecondCommentService {

    private final SecondCommentServiceRepository secondCommentServiceRepository;
    private final CommentRepository commentRepository;

    public ResponseEntity<RestApiResponseDto> createSecondComment(Long id, SecondCommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("해당 댓글이 없습니다."));

       SecondComments secondComments = new SecondComments(requestDto.getContent(),comment,user);
       secondCommentServiceRepository.save(secondComments);
        return this.resultResponse(HttpStatus.OK,"댓글 작성 완료",new SecondCommentResponseDto(secondComments));
    }

    @Transactional
    public ResponseEntity<RestApiResponseDto> updateComment(Long id, SecondCommentRequestDto requestDto, User user) {
        // 댓글이 있는지
        SecondComments secondComments = secondCommentServiceRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 없습니다."));

        // 댓글 작성자 혹은 관리자인지
        Long writerId = secondComments.getUser().getId();
        Long loginId = user.getId();
        if(!writerId.equals(loginId) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        }

        // 댓글 수정
        secondComments.update(requestDto);
        return this.resultResponse(HttpStatus.OK,"댓글 수정 완료",new SecondCommentResponseDto(secondComments));
    }


    public ResponseEntity<RestApiResponseDto> deleteComment(Long id, User user) {
        // 댓글이 있는지
        SecondComments secondComments = secondCommentServiceRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 없습니다."));

        // 댓글 작성자 혹은 관리자인지
        Long writerId = secondComments.getUser().getId();
        Long loginId = user.getId();

        if(!writerId.equals(loginId) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        }

        // 댓글 삭제
        secondCommentServiceRepository.delete(secondComments);
        return this.resultResponse(HttpStatus.OK,"댓글 삭제 완료",null);
    }
    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }
}
