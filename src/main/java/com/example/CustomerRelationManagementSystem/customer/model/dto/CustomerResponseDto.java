package com.example.CustomerRelationManagementSystem.customer.model.dto;

import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionResponseDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SaleResponseDto;
import com.example.CustomerRelationManagementSystem.security.model.dto.UserDetailsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CustomerResponseDto {

    private String publicId;

    private String customerName;

    private String email;

    private String address;

    private String phone;

    private List<SaleResponseDto> sales;

    private List<CustomerInteractionResponseDto> customerInteractions;

    private UserDetailsResponseDto salesRep;
}
