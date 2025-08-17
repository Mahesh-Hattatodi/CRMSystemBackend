package com.example.CustomerRelationManagementSystem.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDetailsResponseDto {

    private String publicId;
    private String username;
    private String email;
}
