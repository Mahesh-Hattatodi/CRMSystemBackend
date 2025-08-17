package com.example.CustomerRelationManagementSystem.sales.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class SaleResponseDto {

    private String publicId;

    private String productName;

    private Double amount;

    private LocalDate date;
}
