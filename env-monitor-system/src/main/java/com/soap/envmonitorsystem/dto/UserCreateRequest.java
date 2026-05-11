package com.soap.envmonitorsystem.dto;

import com.soap.envmonitorsystem.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size(max = 64) String username,
        @NotBlank @Size(min = 4, max = 64) String password,
        @Size(max = 128) String displayName,
        @NotNull Role role
) {}
