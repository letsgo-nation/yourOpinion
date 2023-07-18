package com.example.youropinion.service;

import com.example.youropinion.dto.PostRequestDto;
import com.example.youropinion.dto.PostResponseDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import com.example.youropinion.entity.UserRoleEnum;
import com.example.youropinion.exception.PostNotFoundException;
import com.example.youropinion.repository.CommentRepository;
import com.example.youropinion.repository.PostRepository;
import com.example.youropinion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResponseEntity<RestApiResponseDto> getPostList() {
        List<Post> postList = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = postList.stream()
                .map(PostResponseDto::new)
                .toList();
        return this.resultResponse(HttpStatus.OK,"게시글 전체 조회",postResponseDtoList);
    }

    public ResponseEntity<RestApiResponseDto> getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
        PostResponseDto responseDto = new PostResponseDto(post);

        responseDto.setCommentResponseDtoList(commentRepository.findAllByPostIdOrderByCreatedAtDesc(id));
        return this.resultResponse(HttpStatus.OK,"게시글 상세 조회",responseDto);
    }


    public ResponseEntity<RestApiResponseDto> createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto,user);
        postRepository.save(post);
        return this.resultResponse(HttpStatus.OK,"게시글 작성 완료",new PostResponseDto(post));
    }

    @Transactional
    public ResponseEntity<RestApiResponseDto> updatePost(Long id, PostRequestDto requestDto, User user) {
        // 게시글이 있는지
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다"));

        // 게시글 작성자인지
        Long writerId = post.getUser().getId(); // 게시글 작성자 id
        Long loginId = user.getId(); // 현재 로그인한 id
        // 게시글 작성자가 아니고, 관리자도 아닐 경우
        if(!writerId.equals(loginId) && user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        }

        // 게시글 내용 수정
        post.update(requestDto);
        return this.resultResponse(HttpStatus.OK,"게시글 수정 완료",new PostResponseDto(post));
    }

    public ResponseEntity<RestApiResponseDto> deletePost(Long id, User user) {
        // 게시글이 있는지
        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 없습니다."));

        // 게시글 작성자인지
        Long writerId = post.getUser().getId();
        Long loginId = user.getId();
        // 작성자가 아니고 관리자도 아님 경우 -> true && true
        // 작성자는 아니지만 관리자일 경우 -> 수정 가능. true && false
        if(!writerId.equals(loginId) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자 혹은 관리자만 삭제/수정 할 수 있습니다.");
        }

        // 게시글 내용 삭제
        postRepository.delete(post);
        return this.resultResponse(HttpStatus.OK,"게시글 삭제 완료",null);
    }


    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }

}
