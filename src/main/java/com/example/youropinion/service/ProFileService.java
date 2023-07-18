package com.example.youropinion.service;

import com.example.youropinion.dto.ProFileRequestDto;
import com.example.youropinion.dto.ProFileResponseDto;
import com.example.youropinion.dto.pwChangeRequestDto;
import com.example.youropinion.dto.pwChangeResponseDto;
import com.example.youropinion.entity.User;
import com.example.youropinion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProFileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProFileResponseDto getUsers(String username) {
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalStateException("회원정보가 존재하지 않습니다."));
        return new ProFileResponseDto(user);
    }

    public ProFileResponseDto updateProfile(String username, ProFileRequestDto updateProfile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        user.setNickname(updateProfile.getNickname());
        user.setEmail(updateProfile.getEmail());
        user.setIntroduce(updateProfile.getIntroduce());

        user = userRepository.save(user);
        log.info("프로필 수정이 완료되었습니다.");

        return new ProFileResponseDto(user);
    }

    public pwChangeResponseDto changePassword(String username, pwChangeRequestDto updateProfile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        String checkPassword = updateProfile.getCheckPassword();

        if (!passwordEncoder.matches(checkPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String newPassword = updateProfile.getNewPassword();

        if (isPreviousPassword(user, newPassword)) {
            throw new IllegalArgumentException("최근 변경한 3개의 비밀번호와 중복됩니다. 다른 비밀번호로 작성해주세요.");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);

        // 비밀번호 변경 시 이전 비밀번호를 기록하고 최대 3개까지 유지
        user.getPreviousPasswords().add(hashedPassword);
        if (user.getPreviousPasswords().size() > 3) {
            user.getPreviousPasswords().remove(0);
        }

        user.setPassword(hashedPassword);
        user = userRepository.save(user);
        log.info("비밀번호 변경이 완료되었습니다.");

        return new pwChangeResponseDto(user);
    }

    private boolean isPreviousPassword(User user, String newPassword) {
        for (String prevPassword : user.getPreviousPasswords()) {
            if (passwordEncoder.matches(newPassword, prevPassword)) {
                return true;
            }
        }
        return false;
    }
}
