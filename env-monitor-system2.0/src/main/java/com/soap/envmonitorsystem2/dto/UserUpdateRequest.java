package com.soap.envmonitorsystem2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 4) String password,
        String displayName,
        @NotBlank String role
) {
}
