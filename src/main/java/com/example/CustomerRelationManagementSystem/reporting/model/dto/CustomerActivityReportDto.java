package com.example.CustomerRelationManagementSystem.reporting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerActivityReportDto {

    private String customerName;
    private long totalInteractions;
    private long totalPurchases;
    private double totalRevenue;
}
