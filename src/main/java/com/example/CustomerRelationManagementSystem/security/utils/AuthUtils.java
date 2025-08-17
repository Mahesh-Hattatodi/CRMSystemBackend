package com.example.CustomerRelationManagementSystem.security.utils;

import com.example.CustomerRelationManagementSystem.security.model.UserPrinciple;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    private final UserRepository userRepository;

    @Autowired
    public AuthUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No user is currently authenticated");
        }
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        return userRepository.findByPublicId(userPrinciple.getPublicId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + userPrinciple.getPublicId()));
    }
}
