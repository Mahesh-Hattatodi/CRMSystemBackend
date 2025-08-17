package com.example.CustomerRelationManagementSystem.customer.model.entity;

import com.example.CustomerRelationManagementSystem.interactions.model.entity.CustomerInteraction;
import com.example.CustomerRelationManagementSystem.sales.model.entity.Sale;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "public_id", unique = true, updatable = false, nullable = false)
    private String publicId;

    @Column(name = "customer_name", unique = true, nullable = false)
    private String customerName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Sale> sales;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerInteraction> customerInteractions;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id")
    private User salesRep;

    @PrePersist
    public void generatePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID().toString();
        }
    }
}
