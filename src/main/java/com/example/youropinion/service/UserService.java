package com.example.youropinion.service;

import com.example.youropinion.dto.RestApiResponseDto;
import com.example.youropinion.dto.SignupRequestDto;
import com.example.youropinion.dto.admin.AdminUserResponseDto;
import com.example.youropinion.dto.admin.RoleChangeRequestDto;
import com.example.youropinion.entity.*;
import com.example.youropinion.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "User Service")
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final OpinionARepository opinionARepository;
    private final OpinionBRepository opinionBRepository;
    private final SecondCommentServiceRepository secondCommentServiceRepository;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        // 1. 입력받은 id와 password 를 저장합니다.
        //    password 는 암호화가 이뤄집니다.
        String inputUsername = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 2. user 테이블에 입력받은 id와 동일한 데이터가 있는지 확인합니다.
        Optional<User> checkUser = userRepository.findByUsername(inputUsername);

        // 2-1. 중복 회원이 있을 경우
        if (checkUser.isPresent()) {
            // 서버 측에 로그를 찍는 역할을 합니다.
            log.error(inputUsername + "와 중복된 사용자가 존재합니다.");
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

//         3. email 중복 확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            log.error(checkEmail + "와 중복된 Email 이 존재합니다.");
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 4. 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (StringUtils.hasText(requestDto.getAdminToken())) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 5. 회원 가입 진행
        User user = new User(requestDto, password, role);

        user.getPreviousPasswords().add(password);
        if (user.getPreviousPasswords().size() > 3) {
            user.getPreviousPasswords().remove(0);
        }

        userRepository.save(user);

        log.info(inputUsername + "님이 회원 가입에 성공하였습니다");
    }


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
        Optional<User> checkUser = userRepository.findById(id);
        if (!checkUser.isPresent()) {
            log.error(id + "번 회원 정보가 업습니다.");
            throw new IllegalArgumentException("없는 회원의 삭제를 요청하였습니다.");
        }

        // 회원 삭제
        User user = checkUser.get();

        // 회원이 쓴 게시글, 댓글 삭제
        List<Comment> comments = commentRepository.findByUser(user);
        commentRepository.deleteAll(comments);
        List<SecondComments> secondComments =  secondCommentServiceRepository.findByUser(user);
        secondCommentServiceRepository.deleteAll(secondComments);
        List<OpinionA> opinionAList = opinionARepository.findByUser(user);
        List<OpinionB> opinionBList = opinionBRepository.findByUser(user);
        opinionARepository.deleteAll(opinionAList);
        opinionBRepository.deleteAll(opinionBList);
        List<Post> posts = postRepository.findByUser(user);
        postRepository.deleteAll(posts);

        userRepository.delete(user);
        return this.resultResponse(HttpStatus.OK, "회원 삭제 완료", null);
    }

    // 회원 권한 수정
    @Transactional
    public ResponseEntity<RestApiResponseDto> changeRoleUser(Long id, RoleChangeRequestDto requestDto) {
        // 수정할 회원이 있는지 조회
        Optional<User> checkUser = userRepository.findById(id);
        if (!checkUser.isPresent()) {
            log.error(id + "번 회원 정보가 없습니다.");
            throw new IllegalArgumentException("회원의 정보가 없습니다.");
        }
        User user = checkUser.get();

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


    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }

}