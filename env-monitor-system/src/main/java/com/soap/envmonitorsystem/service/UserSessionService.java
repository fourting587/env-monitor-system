package com.soap.envmonitorsystem.service;

import com.soap.envmonitorsystem.config.SessionKeys;
import com.soap.envmonitorsystem.entity.Role;
import com.soap.envmonitorsystem.entity.User;
import com.soap.envmonitorsystem.exception.ForbiddenException;
import com.soap.envmonitorsystem.exception.UnauthorizedException;
import com.soap.envmonitorsystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserRepository userRepository;

    public User requireUser(HttpSession session) {
        Long id = (Long) session.getAttribute(SessionKeys.USER_ID);
        if (id == null) {
            throw new UnauthorizedException("请先登录");
        }
        return userRepository.findById(id).orElseThrow(() -> new UnauthorizedException("会话已失效，请重新登录"));
    }

    public User requireAdmin(HttpSession session) {
        User user = requireUser(session);
        if (user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("需要管理员权限");
        }
        return user;
    }
}
