package com.example.CustomerRelationManagementSystem.security.model.entity;

import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleName name;
}
