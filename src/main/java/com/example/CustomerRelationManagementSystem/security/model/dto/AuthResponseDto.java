package com.example.CustomerRelationManagementSystem.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class AuthResponseDto {
;
    private String publicId;
    private String username;
    private String email;
    private Set<String> roles;
    private String accessToken;
    private String refreshToken;
}
