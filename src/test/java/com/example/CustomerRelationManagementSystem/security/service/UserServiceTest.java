package com.example.CustomerRelationManagementSystem.security.service;

import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.UserPrinciple;
import com.example.CustomerRelationManagementSystem.security.model.dto.AuthResponseDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.LoginUserRequestDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.RegisterUserRequestDto;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.repository.UserRepository;
import com.example.CustomerRelationManagementSystem.security.utils.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthUtils authUtils;

    @InjectMocks
    private UserService userService;

    private User adminUser;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminRole = new Role();
        adminRole.setName(RoleName.ADMIN);

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setRoles(Set.of(adminRole));
    }

    @Test
    void testRegisterUser_Success() {

        RegisterUserRequestDto dto = new RegisterUserRequestDto("pub1", "john", "password", "john@email.com", Set.of("SALES_MANAGER"));

        when(authUtils.getLoggedInUser()).thenReturn(adminUser);
        when(roleService.findRoles(dto.getRoles())).thenReturn(Set.of(new Role(1, RoleName.SALES_MANAGER)));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        userService.registerUser(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("john", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void testVerifyUser_Success() {

        LoginUserRequestDto loginDto = new LoginUserRequestDto("john", "password");
        User user = new User();
        user.setUsername("john");

        Authentication authentication = mock(Authentication.class);
        UserPrinciple principal = new UserPrinciple(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtService.generateAccessToken(principal)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(principal)).thenReturn("refresh-token");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        AuthResponseDto response = userService.verifyUser(loginDto);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void testFindUserByPublicId_UserNotFound() {

        when(userRepository.findByPublicId("unknown")).thenReturn(Optional.empty());


        Exception exception = assertThrows(RuntimeException.class, () -> userService.findUserByPublicId("unknown"));
        assertTrue(exception.getMessage().contains("User not found"));
    }
}
