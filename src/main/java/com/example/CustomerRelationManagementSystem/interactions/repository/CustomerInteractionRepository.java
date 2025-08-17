package com.example.CustomerRelationManagementSystem.interactions.repository;

import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionReportDto;
import com.example.CustomerRelationManagementSystem.interactions.model.entity.CustomerInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerInteractionRepository extends JpaRepository<CustomerInteraction, Long> {

    Optional<CustomerInteraction> findByPublicId(String publicId);

    List<CustomerInteraction> findByCustomer_SalesRep_PublicId(String salesRepPublicId);

    void deleteByPublicId(String publicId);

    @Query("SELECT ci.customer.customerName, ci.interactionType, COUNT(ci) " +
            "FROM CustomerInteraction ci " +
            "WHERE ci.date BETWEEN :start AND :end " +
            "GROUP BY ci.customer.customerName, ci.interactionType")
    List<CustomerInteractionReportDto> getInteractionReport(LocalDate start, LocalDate end);

}
