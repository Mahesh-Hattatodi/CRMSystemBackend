package com.example.CustomerRelationManagementSystem.interactions.model;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.AddInteractionRequestDto;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionResponseDto;
import com.example.CustomerRelationManagementSystem.interactions.model.entity.CustomerInteraction;

public class CustomerInteractionMapper {

    public static CustomerInteractionResponseDto toDto(CustomerInteraction customerInteraction) {
        return CustomerInteractionResponseDto.builder()
                .publicId(customerInteraction.getPublicId())
                .interactionType(customerInteraction.getInteractionType())
                .notes(customerInteraction.getNotes())
                .date(customerInteraction.getDate())
                .build();
    }

    public static CustomerInteraction toEntity(AddInteractionRequestDto dto, Customer customer) {
        return CustomerInteraction.builder()
                .interactionType(dto.getInteractionType())
                .notes(dto.getNotes())
                .date(dto.getDate())
                .customer(customer)
                .build();
    }
}
