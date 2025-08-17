package com.example.CustomerRelationManagementSystem.interactions.model.dto;

import com.example.CustomerRelationManagementSystem.interactions.model.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class CustomerInteractionResponseDto {
    private String publicId;

    private InteractionType interactionType;

    private String notes;

    private LocalDate date;
}
