package com.example.CustomerRelationManagementSystem.security.repository;

import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
