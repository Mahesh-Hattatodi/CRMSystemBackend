package com.example.CustomerRelationManagementSystem.sales.model;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.sales.model.dto.AddSaleRecordRequestDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SaleResponseDto;
import com.example.CustomerRelationManagementSystem.sales.model.entity.Sale;

public class SaleMapper {

    public static SaleResponseDto toDto(Sale sale) {
        return SaleResponseDto.builder()
                .publicId(sale.getPublicId())
                .productName(sale.getProductName())
                .amount(sale.getAmount())
                .date(sale.getDate())
                .build();
    }

    public static Sale toEntity(AddSaleRecordRequestDto dto, Customer customer) {
        return Sale.builder()
                .productName(dto.getProductName())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .customer(customer)
                .build();
    }
}
