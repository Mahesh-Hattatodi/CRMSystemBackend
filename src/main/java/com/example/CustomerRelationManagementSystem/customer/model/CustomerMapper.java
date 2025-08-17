package com.example.CustomerRelationManagementSystem.customer.model;

import com.example.CustomerRelationManagementSystem.customer.model.dto.AddCustomerRequestDto;
import com.example.CustomerRelationManagementSystem.customer.model.dto.CustomerResponseDto;
import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.interactions.model.CustomerInteractionMapper;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionResponseDto;
import com.example.CustomerRelationManagementSystem.interactions.model.entity.CustomerInteraction;
import com.example.CustomerRelationManagementSystem.sales.model.SaleMapper;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SaleResponseDto;
import com.example.CustomerRelationManagementSystem.sales.model.entity.Sale;
import com.example.CustomerRelationManagementSystem.security.model.UserMapper;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static Customer toEntity(AddCustomerRequestDto dto, User salesRep) {
        return Customer.builder()
                .customerName(dto.getCustomerName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .salesRep(salesRep)
                .build();
    }

    public static CustomerResponseDto toDto(Customer customer) {
        if (customer == null) return null;

        return CustomerResponseDto.builder()
                .publicId(customer.getPublicId())
                .customerName(customer.getCustomerName())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .sales(getSalesResponseDtos(customer.getSales()))
                .customerInteractions(getCustomerInteractionResponseDtos(customer.getCustomerInteractions()))
                .salesRep(customer.getSalesRep() != null
                        ? UserMapper.toUserDetailsResponseDto(customer.getSalesRep())
                        : null)
                .build();
    }

    private static List<SaleResponseDto> getSalesResponseDtos(List<Sale> sales) {
        if (sales == null || sales.isEmpty()) return List.of();
        return sales.stream()
                .map(SaleMapper::toDto)
                .collect(Collectors.toList());
    }

    private static List<CustomerInteractionResponseDto> getCustomerInteractionResponseDtos(
            List<CustomerInteraction> customerInteractions
    ) {
        if (customerInteractions == null || customerInteractions.isEmpty()) return List.of();
        return customerInteractions.stream()
                .map(CustomerInteractionMapper::toDto)
                .collect(Collectors.toList());
    }

}
