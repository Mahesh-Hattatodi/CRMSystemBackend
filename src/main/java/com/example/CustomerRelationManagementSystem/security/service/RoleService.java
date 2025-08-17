package com.example.CustomerRelationManagementSystem.security.service;

import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Set<Role> findRoles(Set<String> roles) {
        return roles.stream()
                .map(roleString -> {
                    RoleName roleName;
                    try {
                        roleName = RoleName.valueOf(roleString);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid role: " + roleString);
                    }

                    return roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found in DB: " + roleString));
                })
                .collect(Collectors.toSet());
    }
}
