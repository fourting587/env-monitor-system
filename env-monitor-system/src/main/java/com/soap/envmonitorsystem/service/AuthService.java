package com.soap.envmonitorsystem.service;

import com.soap.envmonitorsystem.dto.UserProfile;
import com.soap.envmonitorsystem.entity.User;
import com.soap.envmonitorsystem.exception.UnauthorizedException;
import com.soap.envmonitorsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfile login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("用户名或密码错误"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnauthorizedException("用户名或密码错误");
        }
        return new UserProfile(user.getId(), user.getUsername(), user.getDisplayName(), user.getRole());
    }

    public String encodePassword(String raw) {
        return passwordEncoder.encode(raw);
    }
}
