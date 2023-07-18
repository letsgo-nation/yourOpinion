package com.example.youropinion.security;

import com.example.youropinion.entity.User;
import com.example.youropinion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String inputUsername) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(inputUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + inputUsername));
        return new UserDetailsImpl(user);
    }

}
