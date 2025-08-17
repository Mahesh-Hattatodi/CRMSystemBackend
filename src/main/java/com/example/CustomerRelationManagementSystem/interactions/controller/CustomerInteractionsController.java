package com.example.CustomerRelationManagementSystem.interactions.controller;

import com.example.CustomerRelationManagementSystem.interactions.model.dto.AddInteractionRequestDto;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionResponseDto;
import com.example.CustomerRelationManagementSystem.interactions.service.CustomerInteractionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interactions")
@RequiredArgsConstructor
public class CustomerInteractionsController {

    private final CustomerInteractionsService interactionService;

    @PostMapping
    public ResponseEntity<CustomerInteractionResponseDto> createInteraction(@RequestBody AddInteractionRequestDto dto) {
        CustomerInteractionResponseDto response = interactionService.createInteraction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{interactionPublicId}")
    public ResponseEntity<CustomerInteractionResponseDto> getInteraction(@PathVariable String interactionPublicId) {
        CustomerInteractionResponseDto response = interactionService.getCustomerInteraction(interactionPublicId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerInteractionResponseDto>> getAllInteractions() {
        List<CustomerInteractionResponseDto> responses = interactionService.getAllInteractions();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{interactionPublicId}")
    public ResponseEntity<CustomerInteractionResponseDto> updateInteraction(
            @PathVariable String interactionPublicId,
            @RequestBody AddInteractionRequestDto updatedInteraction
    ) {
        CustomerInteractionResponseDto response = interactionService.updateInteraction(interactionPublicId, updatedInteraction);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{interactionPublicId}")
    public ResponseEntity<?> deleteInteraction(@PathVariable String interactionPublicId) {
        interactionService.deleteInteraction(interactionPublicId);
        return ResponseEntity.ok("Interaction deleted successfully");
    }
}
