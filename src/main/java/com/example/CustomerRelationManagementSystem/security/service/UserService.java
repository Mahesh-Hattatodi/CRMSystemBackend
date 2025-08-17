package com.example.CustomerRelationManagementSystem.security.service;

import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.UserMapper;
import com.example.CustomerRelationManagementSystem.security.model.UserPrinciple;
import com.example.CustomerRelationManagementSystem.security.model.dto.AuthResponseDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.LoginUserRequestDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.NewAccessTokenDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.RegisterUserRequestDto;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.repository.UserRepository;
import com.example.CustomerRelationManagementSystem.security.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthUtils authUtils;

    public void registerUser(RegisterUserRequestDto dto) {
        log.info("Attempting to register user: {}", dto.getUsername());

        Set<Role> roles = roleService.findRoles(dto.getRoles());
        log.info("Roles resolved for user {}: {}", dto.getUsername(), roles);

        if (roles.size() != 1) {
            throw new IllegalStateException("Expected exactly one role but found: " + roles.size());
        }

        User loggedInUser = authUtils.getLoggedInUser();
        RoleName newUserRole = roles.iterator().next().getName();
        log.info("Logged in user {} attempting to register role: {}", loggedInUser.getUsername(), newUserRole);

        if (loggedInUser.hasRole(RoleName.ADMIN)) {
            if (!(newUserRole == RoleName.ADMIN
                    || newUserRole == RoleName.SALES_MANAGER
                    || newUserRole == RoleName.DATA_ANALYST)) {
                throw new RuntimeException("Admin can only register Admin, Sales Manager, or Data Analyst.");
            }
        } else if (loggedInUser.hasRole(RoleName.SALES_MANAGER)) {
            if (newUserRole != RoleName.SALES_REPRESENTATIVE) {
                throw new RuntimeException("Sales Manager can only register Sales Representatives.");
            }
        } else {
            throw new RuntimeException("You are not authorized to register users.");
        }

        dto = new RegisterUserRequestDto(
                dto.getPublicId(),
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getEmail(),
                dto.getRoles()
        );

        User manager = (newUserRole == RoleName.SALES_REPRESENTATIVE) ? loggedInUser : null;
        User user = UserMapper.toEntity(dto, roles, manager);
        userRepository.save(user);

        log.info("User registered successfully: {}", dto.getUsername());
    }

    public AuthResponseDto verifyUser(LoginUserRequestDto user) {
        log.info("Authenticating user: {}", user.getUsername());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userPrinciple);
        String refreshToken = jwtService.generateRefreshToken(userPrinciple);

        User verifiedUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found in DB"));

        log.info("User authenticated successfully: {}", user.getUsername());
        return UserMapper.toResponseDto(verifiedUser, accessToken, refreshToken);
    }

    public NewAccessTokenDto refreshAccessToken(String refreshToken) {
        log.info("Refreshing access token");

        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username " + username));

        UserPrinciple userPrinciple = new UserPrinciple(user);
        String accessToken = jwtService.generateAccessToken(userPrinciple);

        log.info("Access token refreshed successfully for user: {}", username);
        return new NewAccessTokenDto(accessToken);
    }

    public User findUserByPublicId(String publicId) {
        log.info("Fetching user by publicId: {}", publicId);
        return userRepository.findByPublicId(publicId)
                .orElseThrow(() -> {
                    log.error("User not found with publicId: {}", publicId);
                    return new EntityNotFoundException("User not found with public id " + publicId);
                });
    }
}

