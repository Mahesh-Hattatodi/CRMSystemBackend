package com.example.CustomerRelationManagementSystem.sales.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesRepRevenueDto {
    private String salesRepUsername;
    private Double totalRevenue;
}
