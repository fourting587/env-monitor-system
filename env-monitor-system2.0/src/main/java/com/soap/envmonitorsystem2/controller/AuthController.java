package com.soap.envmonitorsystem2.controller;

import com.soap.envmonitorsystem2.config.SessionKeys;
import com.soap.envmonitorsystem2.dto.ApiResult;
import com.soap.envmonitorsystem2.dto.LoginRequest;
import com.soap.envmonitorsystem2.dto.UserProfile;
import com.soap.envmonitorsystem2.service.AuthService;
import com.soap.envmonitorsystem2.service.UserSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserSessionService userSessionService;

    public AuthController(AuthService authService, UserSessionService userSessionService) {
        this.authService = authService;
        this.userSessionService = userSessionService;
    }

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
