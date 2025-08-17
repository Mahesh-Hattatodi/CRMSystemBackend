package com.example.CustomerRelationManagementSystem.sales.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddSaleRecordRequestDto {

    private String publicId;

    private String productName;

    private Double amount;

    private LocalDate date;

    private String customerPublicId;
}
