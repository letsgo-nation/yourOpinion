package com.example.youropinion.service;

import com.example.youropinion.dto.CheckOpinionDto;
import com.example.youropinion.dto.OpinionResponseDto;
import com.example.youropinion.entity.OpinionA;
import com.example.youropinion.entity.OpinionB;
import com.example.youropinion.entity.Post;
import com.example.youropinion.entity.User;
import com.example.youropinion.jwt.JwtUtil;
import com.example.youropinion.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpinionService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final OpinionARepository opinionARepository;
    private final OpinionBRepository opinionBRepository;
    private final JwtUtil jwtUtil;

    // OpinionA 추가
    @Transactional
    public OpinionResponseDto increaseOpinionA(Long id, User user) {
        // postRepository에서 user Id를 찾는 메소드
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
                // User가 좋아요를 누른 게시물이 존재하는지 확인. 존재하지 않을 경우, IllegalArgumentException를 던진다.
        );

        // 옵션 중복 투표 방지
        Optional<OpinionB> opinionB = opinionBRepository.findByUserAndPost(user, post);
        if(opinionB.isPresent() && opinionB.get().isOpinionB()) {
            throw new IllegalArgumentException("다른 옵션에 이미 투표하였습니다.");
        }

        // Optional: 단 건 조회를 할 때 쓰인다.
        Optional<OpinionA> checkUserAndPost = opinionARepository.findByUserAndPost(user, post);
        // 좋아요를 누른 user, post를 찾는다.

        // 테이블에 data 가 있을 경우
        if (checkUserAndPost.isPresent()) {
//            필드가 true 일 경우
            if (checkUserAndPost.get().isOpinionA()) {
                throw new IllegalArgumentException("Opinion A를 이미 선택하였습니다");
            } else {
                // liked 필드가 false 일 경우
                OpinionA opinionA = checkUserAndPost.get();
                opinionA.changeOpinion();
                post.increaseOpinionA();
            }
            // 테이블에 data 가 없을 경우
        } else {
            OpinionA opinionA = new OpinionA(user, post);
            opinionA.changeOpinion();
            post.increaseOpinionA();
            opinionARepository.save(new OpinionA(user, post));
        }
        return new OpinionResponseDto("Opinion A을 선택하였습니다.", 200);
    }

    //  OpinionA 취소
    @Transactional
    public OpinionResponseDto decreaseOpinionA(Long id, User user) {
        // postRepository에서 user Id를 찾는 메소드
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
                // User가 좋아요를 누른 게시물이 존재하는지 확인. 존재하지 않을 경우, IllegalArgumentException를 던진다.
        );

        // Optional: 단 건 조회를 할 때 쓰인다.
        Optional<OpinionA> checkUserAndPost = opinionARepository.findByUserAndPost(user, post);
        // 좋아요를 누른 user, post를 찾는다.

        if (checkUserAndPost.isPresent()) {
            if (checkUserAndPost.get().isOpinionA()) {
                OpinionA opinionA = checkUserAndPost.get();
                opinionA.changeOpinion();
                post.decreaseOpinionA();
            } else { // liked 칼럼이 false 일 경우
                throw new IllegalArgumentException("Opinion A을 이미 취소하셨습니다.");
            }
        } else { // 좋아요가 없을 경우
            throw new IllegalArgumentException("Opinion A에 대한 데이터가 없습니다.");
        }
        return new OpinionResponseDto("Opinion A을 취소하였습니다.", 200);
    }




    // OpinionB 추가
    @Transactional
    public OpinionResponseDto increaseOpinionB(Long id, User user) {
        // postRepository에서 user Id를 찾는 메소드
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
                // User가 좋아요를 누른 게시물이 존재하는지 확인. 존재하지 않을 경우, IllegalArgumentException를 던진다.
        );

        // 옵션 중복 투표 방지
        Optional<OpinionA> opinionA = opinionARepository.findByUserAndPost(user, post);
        if(opinionA.isPresent() && opinionA.get().isOpinionA()) {
            throw new IllegalArgumentException("다른 옵션에 이미 투표하였습니다.");
        }

        // Optional: 단 건 조회를 할 때 쓰인다.
        Optional<OpinionB> checkUserAndPost = opinionBRepository.findByUserAndPost(user, post);
        // 좋아요를 누른 user, post를 찾는다.

        // 테이블에 data 가 있을 경우
        if (checkUserAndPost.isPresent()) {
//            필드가 true 일 경우
            if (checkUserAndPost.get().isOpinionB()) {
                throw new IllegalArgumentException("Opinion B를 이미 선택하였습니다");
            } else {
                // liked 필드가 false 일 경우
                OpinionB opinionB = checkUserAndPost.get();
                opinionB.changeOpinion();
                post.increaseOpinionB();
            }
            // 테이블에 data 가 없을 경우
        } else {
            OpinionB opinionB = new OpinionB(user, post);
            opinionB.changeOpinion();
            post.increaseOpinionB();
            opinionBRepository.save(new OpinionB(user, post));
        }
        return new OpinionResponseDto("Opinion B을 선택하였습니다.", 200);
    }

    // OpinionA 취소
    @Transactional
    public OpinionResponseDto decreaseOpinionB(Long id, User user) {
        // postRepository에서 user Id를 찾는 메소드
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
                // User가 좋아요를 누른 게시물이 존재하는지 확인. 존재하지 않을 경우, IllegalArgumentException를 던진다.
        );

        // Optional: 단 건 조회를 할 때 쓰인다.
        Optional<OpinionB> checkUserAndPost = opinionBRepository.findByUserAndPost(user, post);
        // 좋아요를 누른 user, post를 찾는다.

        if (checkUserAndPost.isPresent()) {
            if (checkUserAndPost.get().isOpinionB()) {
                OpinionB opinionB = checkUserAndPost.get();
                opinionB.changeOpinion();
                post.decreaseOpinionB();
            } else { // liked 칼럼이 false 일 경우
                throw new IllegalArgumentException("Opinion B을 이미 취소하셨습니다.");
            }
        } else { // 좋아요가 없을 경우
            throw new IllegalArgumentException("Opinion B에 대한 데이터가 없습니다.");
        }
        return new OpinionResponseDto("Opinion B을 취소하였습니다.", 200);
    }

    // 투표 값 여부 반환 메서드
    public CheckOpinionDto checkValue(Long id, User user) {
        Post post = postRepository.findById(id).get();

        Optional<OpinionA> opinionA = opinionARepository.findByUserAndPost(user,post);
        Optional<OpinionB> opinionB = opinionBRepository.findByUserAndPost(user,post);
        CheckOpinionDto checkOpinionDto = new CheckOpinionDto();

        // 옵션 1
        if(opinionA.isPresent()){ // 옵션1에 투표 혹은 투표취소한 데이터가 있다면
            checkOpinionDto.setOpinionAresult(opinionA.get().isOpinionA());
            log.info("옵션A의 투표 여부: "+ checkOpinionDto.isOpinionAresult());
        }else { // 투표 내역이 없다면
            checkOpinionDto.setOpinionAresult(false);
            log.info("옵션A의 투표 여부: "+ checkOpinionDto.isOpinionAresult());
        }

        // 옵션 2
        if(opinionB.isPresent()){
            checkOpinionDto.setOpinionBresult(opinionB.get().isOpinionB());
            log.info("옵션B의 투표 여부: "+ checkOpinionDto.isOpinionBresult());
        }else {
            checkOpinionDto.setOpinionBresult(false);
            log.info("옵션B의 투표 여부: "+ checkOpinionDto.isOpinionBresult());
        }
        return checkOpinionDto;
    }


}