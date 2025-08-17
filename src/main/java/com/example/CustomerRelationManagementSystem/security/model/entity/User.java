package com.example.CustomerRelationManagementSystem.security.model.entity;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "public_id", unique = true, updatable = false, nullable = false)
    private String publicId = UUID.randomUUID().toString();

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager; // many sales reps have one manager.

    @OneToMany(mappedBy = "manager")
    private Set<User> salesRep; // one manager can have many sales representatives.

    @OneToMany(mappedBy = "salesRep")
    private Set<Customer> customers; // one sales representative can have many customers assigned.

    public boolean hasRole(RoleName roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName() == roleName);
    }

    @PrePersist
    public void generatePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID().toString();
        }
    }
}
