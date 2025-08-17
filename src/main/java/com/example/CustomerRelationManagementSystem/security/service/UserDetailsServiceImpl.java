package com.example.CustomerRelationManagementSystem.security.service;

import com.example.CustomerRelationManagementSystem.security.model.UserPrinciple;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User record not found with username " + username));

        return new UserPrinciple(user);
    }
}
