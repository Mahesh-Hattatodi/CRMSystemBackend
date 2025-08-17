package com.example.CustomerRelationManagementSystem.app_config;

import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Ensuring all the roles exist in the database at the application startup
        for (RoleName roleName: RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(roleName);
                        return roleRepository.save(newRole);
                    });
        }
    }
}
