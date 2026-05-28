package com.soap.envmonitorsystem2.service;

import com.soap.envmonitorsystem2.dto.UserCreateRequest;
import com.soap.envmonitorsystem2.dto.UserProfile;
import com.soap.envmonitorsystem2.dto.UserUpdateRequest;
import com.soap.envmonitorsystem2.entity.User;
import com.soap.envmonitorsystem2.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserProfile> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toProfile);
    }

    public UserProfile create(UserCreateRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User user = new User();
        user.setUsername(req.username());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setDisplayName(req.displayName() == null || req.displayName().isBlank() ? req.username() : req.displayName());
        user.setRole(req.role());
        user.setCreatedAt(LocalDateTime.now());
        return toProfile(userRepository.save(user));
    }

    public UserProfile update(Long id, UserUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (req.password() != null && !req.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(req.password()));
        }
        if (req.displayName() != null) {
            user.setDisplayName(req.displayName().isBlank() ? user.getUsername() : req.displayName());
        }
        user.setRole(req.role());
        return toProfile(userRepository.save(user));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserProfile toProfile(User user) {
        return new UserProfile(user.getId(), user.getUsername(), user.getDisplayName(), user.getRole());
    }
}
