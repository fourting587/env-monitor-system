package com.soap.envmonitorsystem2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 4) String password,
        String displayName,
        @NotBlank String role
) {
}
