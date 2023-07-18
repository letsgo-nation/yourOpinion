package com.example.youropinion.service;

import com.example.youropinion.dto.ProFileRequestDto;
import com.example.youropinion.dto.ProFileResponseDto;
import com.example.youropinion.entity.User;
import com.example.youropinion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProFileResponseDto getUsers(String username) {
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalStateException("회원정보가 존재하지않습니다."));
        return new ProFileResponseDto(user);
    }

    public ProFileResponseDto updateProfile(String username, ProFileRequestDto updateProfile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        user.setNickname(updateProfile.getNickname());
        user.setEmail(updateProfile.getEmail());
        user.setIntroduce(updateProfile.getIntroduce());

        user = userRepository.save(user);

        return new ProFileResponseDto(user);
    }

    public ProFileResponseDto changePassword(String username, ProFileRequestDto updateProfile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        String checkPassword = updateProfile.getCheckPassword();

        if (!passwordEncoder.matches(checkPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String newPassword = updateProfile.getNewPassword();

        if (isPreviousPassword(user, newPassword)) {
            throw new IllegalArgumentException("최근 3번의 비밀번호와 중복됩니다.");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);

        // 비밀번호 변경 시 이전 비밀번호를 기록하고 최대 3개까지 유지
        user.getPreviousPasswords().add(hashedPassword);
        if (user.getPreviousPasswords().size() > 3) {
            user.getPreviousPasswords().remove(0);
        }

        user.setPassword(hashedPassword);
        user = userRepository.save(user);

        return new ProFileResponseDto(user);
    }

    private boolean isPreviousPassword(User user, String newPassword) {
        String hashedNewPassword = passwordEncoder.encode(newPassword);
        return user.getPreviousPasswords().contains(hashedNewPassword);
    }
}
