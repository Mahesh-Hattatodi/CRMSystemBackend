package com.example.CustomerRelationManagementSystem.app_config;

import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.repository.RoleRepository;
import com.example.CustomerRelationManagementSystem.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(2)
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        // Creating a default admin credentials
        userRepository.findByUsername("admin").orElseGet(() -> {
            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@gmail.com")
                    .roles(Set.of(adminRole))
                    .build();

            return userRepository.save(admin);
        });
    }
}
