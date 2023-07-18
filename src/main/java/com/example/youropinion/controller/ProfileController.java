package com.example.youropinion.controller;

import com.example.youropinion.dto.ProFileRequestDto;
import com.example.youropinion.dto.ProFileResponseDto;
import com.example.youropinion.dto.UserInfoDto;
import com.example.youropinion.security.UserDetailsImpl;
import com.example.youropinion.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profile")
    public UserInfoDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        String nickname = userDetails.getUser().getNickname();

        String username = userDetails.getUser().getUsername();
        ProFileResponseDto proFileResponseDto = profileService.getUsers(username);

        return new UserInfoDto(nickname, proFileResponseDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<ProFileResponseDto> updateProfile(
            @Valid
            @RequestBody ProFileRequestDto updateProfile,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String loggedInUsername = userDetails.getUser().getUsername();
        if (!loggedInUsername.equals(updateProfile.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProFileResponseDto updatedProfile = profileService.updateProfile(loggedInUsername, updateProfile);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/password")
    public ResponseEntity<ProFileResponseDto> changePassword(
            @Valid
            @RequestBody ProFileRequestDto updateProfile,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String loggedInUsername = userDetails.getUser().getUsername();
        if (!loggedInUsername.equals(updateProfile.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProFileResponseDto responseDto = profileService.changePassword(loggedInUsername, updateProfile);
        return ResponseEntity.ok(responseDto);
    }
}
