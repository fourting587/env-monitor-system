package com.soap.envmonitorsystem2.service;

import com.soap.envmonitorsystem2.config.SessionKeys;
import com.soap.envmonitorsystem2.entity.User;
import com.soap.envmonitorsystem2.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {

    private final UserRepository userRepository;

    public UserSessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser(HttpSession session) {
        Object id = session.getAttribute(SessionKeys.USER_ID);
        if (!(id instanceof Long)) {
            throw new IllegalArgumentException("未登录或会话已过期");
        }
        return userRepository.findById((Long) id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    public void requireAdmin(HttpSession session) {
        User user = requireUser(session);
        if (!"ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("权限不足");
        }
    }
}
