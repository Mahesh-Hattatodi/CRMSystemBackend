package com.example.CustomerRelationManagementSystem.interactions.model.dto;

import com.example.CustomerRelationManagementSystem.interactions.model.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInteractionReportDto {
    private String customerName;
    private InteractionType interactionType;
    private Long count;
}
