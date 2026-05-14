package com.soap.envmonitorsystem.service;

import com.soap.envmonitorsystem.dto.UserCreateRequest;
import com.soap.envmonitorsystem.dto.UserProfile;
import com.soap.envmonitorsystem.dto.UserUpdateRequest;
import com.soap.envmonitorsystem.entity.User;
import com.soap.envmonitorsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public Page<UserProfile> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toProfile);
    }

    @Transactional
    public UserProfile create(UserCreateRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPasswordHash(authService.encodePassword(req.password()));
        u.setDisplayName(req.displayName() != null ? req.displayName() : req.username());
        u.setRole(req.role());
        userRepository.save(u);
        return toProfile(u);
    }

    @Transactional
    public UserProfile update(Long id, UserUpdateRequest req) {
        User u = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (req.password() != null && !req.password().isBlank()) {
            u.setPasswordHash(authService.encodePassword(req.password()));
        }
        if (req.displayName() != null) {
            u.setDisplayName(req.displayName());
        }
        if (req.role() != null) {
            u.setRole(req.role());
        }
        return toProfile(u);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        userRepository.deleteById(id);
    }

    private UserProfile toProfile(User u) {
        return new UserProfile(u.getId(), u.getUsername(), u.getDisplayName(), u.getRole());
    }
}
