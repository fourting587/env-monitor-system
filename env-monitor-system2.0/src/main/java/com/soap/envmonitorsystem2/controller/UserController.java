package com.soap.envmonitorsystem2.controller;

import com.soap.envmonitorsystem2.dto.ApiResult;
import com.soap.envmonitorsystem2.dto.UserCreateRequest;
import com.soap.envmonitorsystem2.dto.UserProfile;
import com.soap.envmonitorsystem2.dto.UserUpdateRequest;
import com.soap.envmonitorsystem2.service.UserManagementService;
import com.soap.envmonitorsystem2.service.UserSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserManagementService userManagementService;
    private final UserSessionService userSessionService;

    public UserController(UserManagementService userManagementService, UserSessionService userSessionService) {
        this.userManagementService = userManagementService;
        this.userSessionService = userSessionService;
    }

    @GetMapping
    public ApiResult<Page<UserProfile>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        userSessionService.requireAdmin(session);
        return ApiResult.ok(userManagementService.list(org.springframework.data.domain.PageRequest.of(page, Math.min(size, 100))));
    }

    @PostMapping
    public ApiResult<UserProfile> create(@Valid @RequestBody UserCreateRequest req, HttpSession session) {
        userSessionService.requireAdmin(session);
        return ApiResult.ok(userManagementService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResult<UserProfile> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req, HttpSession session) {
        userSessionService.requireAdmin(session);
        return ApiResult.ok(userManagementService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id, HttpSession session) {
        userSessionService.requireAdmin(session);
        userManagementService.delete(id);
        return ApiResult.ok();
    }
}
