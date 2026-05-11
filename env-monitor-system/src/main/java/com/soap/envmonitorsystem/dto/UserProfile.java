package com.soap.envmonitorsystem.dto;

import com.soap.envmonitorsystem.entity.Role;

public record UserProfile(Long id, String username, String displayName, Role role) {

}
