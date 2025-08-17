package com.example.CustomerRelationManagementSystem.security.model.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDto {

    private String publicId;
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
}
