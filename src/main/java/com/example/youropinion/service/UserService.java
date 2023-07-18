package com.example.youropinion.service;

import com.example.youropinion.dto.SignupRequestDto;
import com.example.youropinion.entity.User;
import com.example.youropinion.entity.UserRoleEnum;
import com.example.youropinion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j(topic = "User Service")
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        if (requestDto.isAdmin()) {
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
}