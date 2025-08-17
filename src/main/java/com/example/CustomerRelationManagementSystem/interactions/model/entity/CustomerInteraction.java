package com.example.CustomerRelationManagementSystem.interactions.model.entity;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.interactions.model.InteractionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CustomerInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, updatable = false, nullable = false)
    private String publicId;

    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    private String notes;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @PrePersist
    public void generatePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID().toString();
        }
    }
}
