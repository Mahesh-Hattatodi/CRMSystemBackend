package com.example.CustomerRelationManagementSystem.security.model;

import com.example.CustomerRelationManagementSystem.security.model.dto.AuthResponseDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.RegisterUserRequestDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.UserDetailsResponseDto;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static AuthResponseDto toResponseDto(User user, String accessToken, String refreshToken) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .publicId(user.getPublicId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .build();
    }

    public static UserDetailsResponseDto toUserDetailsResponseDto(User user) {
        return UserDetailsResponseDto.builder()
                .publicId(user.getPublicId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public static User toEntity(RegisterUserRequestDto dto, Set<Role> roles, User manager) {
        return User.builder()
                .publicId(dto.getPublicId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .roles(roles)
                .manager(manager)
                .build();
    }
}
