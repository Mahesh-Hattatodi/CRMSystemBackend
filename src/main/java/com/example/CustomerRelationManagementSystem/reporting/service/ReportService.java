package com.example.CustomerRelationManagementSystem.reporting.service;

import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionReportDto;
import com.example.CustomerRelationManagementSystem.interactions.repository.CustomerInteractionRepository;
import com.example.CustomerRelationManagementSystem.reporting.repository.ReportRepository;
import com.example.CustomerRelationManagementSystem.sales.model.dto.CustomerRevenueDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SalesRepRevenueDto;
import com.example.CustomerRelationManagementSystem.sales.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportRepository {

    private final SaleRepository saleRepository;
    private final CustomerInteractionRepository interactionRepository;

    @Override
    public List<CustomerRevenueDto> getCustomerRevenueReport(LocalDate start, LocalDate end) {
        return saleRepository.getRevenueReport(start, end);
    }

    @Override
    public List<SalesRepRevenueDto> getSalesRepRevenueReport(LocalDate start, LocalDate end) {
        return saleRepository.getSalesRepRevenueReport(start, end);
    }

    @Override
    public List<CustomerInteractionReportDto> getCustomerInteractionReport(LocalDate start, LocalDate end) {
        return interactionRepository.getInteractionReport(start, end);
    }
}
