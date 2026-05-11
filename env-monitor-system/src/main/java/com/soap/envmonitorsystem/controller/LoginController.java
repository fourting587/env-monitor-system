package com.soap.envmonitorsystem.controller;

import com.soap.envmonitorsystem.config.SessionKeys;
import com.soap.envmonitorsystem.dto.ApiResult;
import com.soap.envmonitorsystem.dto.LoginRequest;
import com.soap.envmonitorsystem.dto.UserProfile;
import com.soap.envmonitorsystem.service.AuthService;
import com.soap.envmonitorsystem.service.UserSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;
    private final UserSessionService userSessionService;

    @PostMapping("/login")
    public ApiResult<UserProfile> login(@Valid @RequestBody LoginRequest req, HttpSession session) {
        UserProfile profile = authService.login(req.username(), req.password());
        session.setAttribute(SessionKeys.USER_ID, profile.id());
        return ApiResult.ok(profile);
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResult.ok();
    }

    @GetMapping("/me")
    public ApiResult<UserProfile> me(HttpSession session) {
        var user = userSessionService.requireUser(session);
        return ApiResult.ok(new UserProfile(user.getId(), user.getUsername(), user.getDisplayName(), user.getRole()));
    }
}
