package com.example.CustomerRelationManagementSystem.interactions.model.dto;

import com.example.CustomerRelationManagementSystem.interactions.model.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddInteractionRequestDto {

    private InteractionType interactionType;

    private String notes;

    private LocalDate date;

    private String customerPublicId;
}
