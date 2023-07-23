package com.example.youropinion.service;

import com.example.youropinion.dto.CommentResponseDto;
import com.example.youropinion.dto.PostResponseDto;
import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.dto.admin.AdminUserResponseDto;
import com.example.youropinion.dto.admin.RoleChangeRequestDto;
import com.example.youropinion.entity.*;
import com.example.youropinion.exception.PostNotFoundException;
import com.example.youropinion.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final OpinionARepository opinionARepository;
    private final OpinionBRepository opinionBRepository;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public List<AdminUserResponseDto> getAdminPageUsers() {
        List<User> users = userRepository.findAll();

        List<AdminUserResponseDto> responseDtoList = new ArrayList<>();
        for (User user : users) {
            int voteCnt = opinionARepository.findByUserAndOpinionA(user, true).size()
                    + opinionBRepository.findByUserAndOpinionB(user, true).size(); // 투표한 내역 갯수
            int postCnt = postRepository.findByUser(user).size(); // 작성한 게시글 갯수
            int commentCnt = commentRepository.findByUser(user).size(); // 작성한 댓글 갯수
            log.info("postCnt: " + postCnt);
            log.info("commentCnt: " + commentCnt);
            log.info("voteCnt: " + voteCnt);

            AdminUserResponseDto responseDto = new AdminUserResponseDto(
                    user, voteCnt, postCnt, commentCnt);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    // 회원 삭제
    public ResponseEntity<RestApiResponseDto> deleteUser(Long id) {
        // 삭제할 회원이 있는지 조회
        User user = findUser(id);

        // 회원이 쓴 게시글, 댓글 삭제
        List<Comment> comments = commentRepository.findByUser(user);
        commentRepository.deleteAll(comments);
        List<OpinionA> opinionAList = opinionARepository.findByUser(user);
        List<OpinionB> opinionBList = opinionBRepository.findByUser(user);
        opinionARepository.deleteAll(opinionAList);
        opinionBRepository.deleteAll(opinionBList);
        List<Post> posts = postRepository.findByUser(user);
        postRepository.deleteAll(posts);

        userRepository.delete(user);
        return this.resultResponse(HttpStatus.OK, "회원 삭제 완료", null);
    }



    // 권한 수정
    @Transactional
    public ResponseEntity<RestApiResponseDto> changeRoleUser(Long id, RoleChangeRequestDto requestDto) {
        // 수정할 회원이 있는지 조회
        User user = findUser(id);

        if (requestDto.isAdmin()) {
            if (user.getRole() == UserRoleEnum.ADMIN) {
                throw new IllegalArgumentException("동일한 권한으로 수정 요청하였습니다.");
            } else {
                if (!StringUtils.hasText(requestDto.getAdminToken())) {
                    throw new IllegalArgumentException("관리자 암호를 입력해주세요");
                } else if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                    throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
                } else if (ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                    user.setRole(UserRoleEnum.ADMIN);
                }
            }
        } else {
            if (user.getRole() == UserRoleEnum.USER) {
                throw new IllegalArgumentException("동일한 권한으로 수정 요청하였습니다.");
            } else {
                if (!StringUtils.hasText(requestDto.getAdminToken())) {
                    throw new IllegalArgumentException("관리자 암호를 입력해주세요");
                } else if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                    throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
                } else if (ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                    user.setRole(UserRoleEnum.USER);
                }
            }
        }
        return resultResponse(HttpStatus.OK, "권한 수정 완료", null);
    }

    // 게시글 조회

    public List<PostResponseDto> getAdminPagePosts() {
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        List<Post> postList = postRepository.findAll();

        for (Post post : postList) {
            PostResponseDto responseDto = new PostResponseDto(post);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    // 게시글 삭제

    public ResponseEntity<RestApiResponseDto> deleteAdminPagePost(Long id) {
        Post post = findPost(id);
        List<Comment> comments = commentRepository.findByPost(post);
        commentRepository.deleteAll(comments);
        List<OpinionA> opinionAList = opinionARepository.findByPost(post);
        opinionARepository.deleteAll(opinionAList);
        List<OpinionB> opinionBList = opinionBRepository.findByPost(post);
        opinionBRepository.deleteAll(opinionBList);
        postRepository.delete(post);

        return this.resultResponse(HttpStatus.OK,"게시글 삭제 완료",null);
    }

    public List<CommentResponseDto> getAdminPageComments() {
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAll();

        for (Comment comment : commentList) {
            CommentResponseDto responseDto = new CommentResponseDto(comment);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    public ResponseEntity<RestApiResponseDto> deleteAdminPageComment(Long id) {
        Comment comment = commentRepository.getReferenceById(id);
        commentRepository.delete(comment);
        return this.resultResponse(HttpStatus.OK,"댓글 삭제 완료",null);
    }



    // 회원 가져오기
    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다"));
    }

    // 게시글 가져오기
    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }


}
