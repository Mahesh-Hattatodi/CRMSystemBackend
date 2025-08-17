package com.example.CustomerRelationManagementSystem.sales.model.entity;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
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
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "public_id", unique = true, updatable = false, nullable = false)
    private String publicId;

    @Column(name = "product_name")
    private String productName;
    private Double amount;
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
