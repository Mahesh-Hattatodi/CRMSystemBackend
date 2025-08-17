package com.example.CustomerRelationManagementSystem.customer.repository;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    void deleteByPublicId(String publicId);

    Optional<Customer> findByPublicId(String publicId);

    boolean existsByPublicIdAndSalesRepId(String publicId, Long salesRepId);

    @Modifying
    @Query("UPDATE Customer c SET c.salesRep = :newRep WHERE c.salesRep.id = :oldRepId")
    void updateSalesRepForCustomer(@Param("oldRepId") String oldRepId, @Param("newRep") User newRep);

}
