package com.soap.envmonitorsystem.dto;

import com.soap.envmonitorsystem.entity.Role;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 4, max = 64) String password,
        @Size(max = 128) String displayName,
        Role role
) {}
