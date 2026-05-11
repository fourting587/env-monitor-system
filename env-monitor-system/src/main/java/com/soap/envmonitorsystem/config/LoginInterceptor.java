package com.soap.envmonitorsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soap.envmonitorsystem.dto.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String uri = request.getRequestURI();
        if ("POST".equalsIgnoreCase(request.getMethod()) && uri.endsWith("/api/auth/login")) {
            return true;
        }
        HttpSession session = request.getSession(false);
        Long uid = session != null ? (Long) session.getAttribute(SessionKeys.USER_ID) : null;
        if (uid == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            objectMapper.writeValue(response.getWriter(), ApiResult.fail("请先登录"));
            return false;
        }
        return true;
    }
}
