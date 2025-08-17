package com.example.CustomerRelationManagementSystem.security.repository;

import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPublicId(String publicId);
}
