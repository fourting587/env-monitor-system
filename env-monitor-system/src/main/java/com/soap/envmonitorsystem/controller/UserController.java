package com.soap.envmonitorsystem.controller;

import com.soap.envmonitorsystem.dto.ApiResult;
import com.soap.envmonitorsystem.dto.UserCreateRequest;
import com.soap.envmonitorsystem.dto.UserProfile;
import com.soap.envmonitorsystem.dto.UserUpdateRequest;
import com.soap.envmonitorsystem.service.UserManagementService;
import com.soap.envmonitorsystem.service.UserSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
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
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;
    private final UserSessionService userSessionService;

    @GetMapping
    public ApiResult<Page<UserProfile>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        userSessionService.requireAdmin(session);
        Page<UserProfile> result = userManagementService.list(PageRequest.of(page, Math.min(size, 100)));
        return ApiResult.ok(result);
    }

    @PostMapping
    public ApiResult<UserProfile> create(@Valid @RequestBody UserCreateRequest req, HttpSession session) {
        userSessionService.requireAdmin(session);
        return ApiResult.ok(userManagementService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResult<UserProfile> update(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody UserUpdateRequest req,
            HttpSession session) {
        userSessionService.requireAdmin(session);
        return ApiResult.ok(userManagementService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable @NonNull Long id, HttpSession session) {
        userSessionService.requireAdmin(session);
        userManagementService.delete(id);
        return ApiResult.ok();
    }
}
