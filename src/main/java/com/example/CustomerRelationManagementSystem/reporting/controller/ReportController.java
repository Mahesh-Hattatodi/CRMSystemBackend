package com.example.CustomerRelationManagementSystem.reporting.controller;

import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionReportDto;
import com.example.CustomerRelationManagementSystem.reporting.model.dto.CustomerActivityReportDto;
import com.example.CustomerRelationManagementSystem.reporting.service.ReportService;
import com.example.CustomerRelationManagementSystem.sales.model.dto.CustomerRevenueDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SalesRepRevenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/customer-revenue")
    public List<CustomerRevenueDto> getCustomerRevenue(@RequestParam LocalDate start,
                                                       @RequestParam LocalDate end) {
        return reportService.getCustomerRevenueReport(start, end);
    }

    @GetMapping("/salesrep-revenue")
    public List<SalesRepRevenueDto> getSalesRepRevenue(@RequestParam LocalDate start,
                                                       @RequestParam LocalDate end) {
        return reportService.getSalesRepRevenueReport(start, end);
    }

    @GetMapping("/customer-interactions")
    public List<CustomerInteractionReportDto> getCustomerInteractions(@RequestParam LocalDate start,
                                                                      @RequestParam LocalDate end) {
        return reportService.getCustomerInteractionReport(start, end);
    }
}
