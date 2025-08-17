package com.example.CustomerRelationManagementSystem.sales.repository;

import com.example.CustomerRelationManagementSystem.sales.model.dto.CustomerRevenueDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SalesRepRevenueDto;
import com.example.CustomerRelationManagementSystem.sales.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByPublicId(String salePublicId);

    void deleteByPublicId(String salePublicId);

    @Query("SELECT s.customer.customerName, SUM(s.amount) as totalRevenue " +
            "FROM Sale s WHERE s.date BETWEEN :start AND :end " +
            "GROUP BY s.customer.customerName")
    List<CustomerRevenueDto> getRevenueReport(LocalDate start, LocalDate end);

    @Query("SELECT s.customer.salesRep.username, SUM(s.amount) " +
            "FROM Sale s WHERE s.date BETWEEN :start AND :end " +
            "GROUP BY s.customer.salesRep.username")
    List<SalesRepRevenueDto> getSalesRepRevenueReport(LocalDate start, LocalDate end);
}
