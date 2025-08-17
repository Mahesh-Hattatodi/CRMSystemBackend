package com.example.CustomerRelationManagementSystem.customer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdateRequest {

    private String customerName;

    private String email;

    private String address;

    private String phone;
}
