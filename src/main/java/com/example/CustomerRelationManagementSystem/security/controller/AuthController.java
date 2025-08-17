package com.example.CustomerRelationManagementSystem.security.controller;

import com.example.CustomerRelationManagementSystem.security.model.dto.AuthResponseDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.LoginUserRequestDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.NewAccessTokenDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.RegisterUserRequestDto;
import com.example.CustomerRelationManagementSystem.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user
     * Admin -> Admin, Sales Manager, Data Analyst
     * Sales Manager -> Sales Representative
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequestDto dto) {
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    /**
     * Login endpoint: verifies credentials and returns JWT tokens
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequestDto loginDto) {
        AuthResponseDto authResponse = userService.verifyUser(loginDto);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        NewAccessTokenDto newAccessToken = userService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }

}
