package com.example.CustomerRelationManagementSystem.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequestDto {

    private String username;
    private String password;
}
