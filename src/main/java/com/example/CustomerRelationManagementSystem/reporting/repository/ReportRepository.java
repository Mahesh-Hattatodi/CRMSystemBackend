package com.example.CustomerRelationManagementSystem.reporting.repository;

import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionReportDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.CustomerRevenueDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SalesRepRevenueDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository {

    List<CustomerRevenueDto> getCustomerRevenueReport(LocalDate start, LocalDate end);

    List<SalesRepRevenueDto> getSalesRepRevenueReport(LocalDate start, LocalDate end);

    List<CustomerInteractionReportDto> getCustomerInteractionReport(LocalDate start, LocalDate end);
}
