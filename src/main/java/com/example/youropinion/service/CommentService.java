package com.example.youropinion.service;

import com.example.youropinion.dto.CommentRequestDto;
import com.example.youropinion.dto.CommentResponseDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.entity.Comment;
import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import com.example.youropinion.entity.UserRoleEnum;
import com.example.youropinion.exception.CommentNotFoundException;
import com.example.youropinion.exception.PostNotFoundException;
import com.example.youropinion.repository.CommentRepository;
import com.example.youropinion.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public ResponseEntity<RestApiResponseDto> createComment(Long id, CommentRequestDto requestDto, User user) {
        // 게시글이 있는지
        Post post = postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("해당 게시글이 없습니다."));

        // 댓글 작성
        Comment comment = new Comment(requestDto,post,user);
        commentRepository.save(comment);
        return this.resultResponse(HttpStatus.OK,"댓글 작성 완료",new CommentResponseDto(comment));
    }


    @Transactional
    public ResponseEntity<RestApiResponseDto> updateComment(Long id, CommentRequestDto requestDto, User user) {
        // 댓글이 있는지
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 없습니다."));

        // 댓글 작성자 혹은 관리자인지
        Long writerId = comment.getId();
        Long loginId = user.getId();
        if(!writerId.equals(loginId) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        }

        // 댓글 수정
        comment.update(requestDto);
        return this.resultResponse(HttpStatus.OK,"댓글 수정 완료",new CommentResponseDto(comment));
    }


    public ResponseEntity<RestApiResponseDto> deleteComment(Long id, User user) {
        // 댓글이 있는지
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글이 없습니다."));

        // 댓글 작성자 혹은 관리자인지
        Long writerId = comment.getId();
        Long loginId = user.getId();
        
        if(!writerId.equals(loginId) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        }

        // 댓글 삭제
        commentRepository.delete(comment);
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
