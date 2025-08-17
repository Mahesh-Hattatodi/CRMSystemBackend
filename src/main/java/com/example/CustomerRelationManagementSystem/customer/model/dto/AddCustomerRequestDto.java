package com.example.CustomerRelationManagementSystem.customer.model.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerRequestDto {

    private String customerName;

    private String email;

    private String address;

    private String phone;

    private String salesRepPublicId;
}
