package com.example.CustomerRelationManagementSystem.interactions.service;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.customer.service.CustomerService;
import com.example.CustomerRelationManagementSystem.interactions.model.CustomerInteractionMapper;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.AddInteractionRequestDto;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionResponseDto;
import com.example.CustomerRelationManagementSystem.interactions.model.entity.CustomerInteraction;
import com.example.CustomerRelationManagementSystem.interactions.repository.CustomerInteractionRepository;
import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerInteractionsService {

    private final CustomerInteractionRepository interactionRepository;
    private final CustomerService customerService;
    private final AuthUtils authUtils;

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    public CustomerInteractionResponseDto createInteraction(AddInteractionRequestDto dto) {
        log.info("Creating interaction for customerPublicId: {}", dto.getCustomerPublicId());

        Customer customer = customerService.findCustomerByPublicId(dto.getCustomerPublicId());
        User loggedInUser = authUtils.getLoggedInUser();

        if (loggedInUser.hasRole(RoleName.SALES_REPRESENTATIVE)
                && !customer.getSalesRep().getPublicId().equals(loggedInUser.getPublicId())) {
            log.warn("Unauthorized attempt by sales rep {} to create interaction for customer {}",
                    loggedInUser.getUsername(), customer.getCustomerName());
            throw new RuntimeException("You are not authorized to add interactions for this customer.");
        }

        CustomerInteraction interaction = CustomerInteractionMapper.toEntity(dto, customer);
        CustomerInteraction savedInteraction = interactionRepository.save(interaction);

        log.info("Interaction created successfully with publicId: {}", savedInteraction.getPublicId());
        return CustomerInteractionMapper.toDto(savedInteraction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'DATA_ANALYST')")
    public CustomerInteractionResponseDto getCustomerInteraction(String interactionPublicId) {
        log.info("Fetching interaction with publicId: {}", interactionPublicId);

        CustomerInteraction interaction = interactionRepository.findByPublicId(interactionPublicId)
                .orElseThrow(() -> {
                    log.error("Interaction not found with publicId: {}", interactionPublicId);
                    return new EntityNotFoundException("Customer Interaction not found with id: " + interactionPublicId);
                });

        User loggedInUser = authUtils.getLoggedInUser();
        if (loggedInUser.hasRole(RoleName.SALES_REPRESENTATIVE)
                && !interaction.getCustomer().getSalesRep().getPublicId().equals(loggedInUser.getPublicId())) {
            log.warn("Unauthorized attempt by sales rep {} to view interaction {}",
                    loggedInUser.getUsername(), interactionPublicId);
            throw new RuntimeException("You are not authorized to view this customer interaction.");
        }

        log.info("Interaction fetched successfully with publicId: {}", interactionPublicId);
        return CustomerInteractionMapper.toDto(interaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'DATA_ANALYST')")
    public List<CustomerInteractionResponseDto> getAllInteractions() {
        User loggedInUser = authUtils.getLoggedInUser();
        log.info("Fetching all interactions for user: {}", loggedInUser.getUsername());

        List<CustomerInteraction> interactions;
        if (loggedInUser.hasRole(RoleName.SALES_REPRESENTATIVE)) {
            interactions = interactionRepository.findByCustomer_SalesRep_PublicId(loggedInUser.getPublicId());
            log.info("Fetched {} interactions assigned to sales rep {}", interactions.size(), loggedInUser.getUsername());
        } else {
            interactions = interactionRepository.findAll();
            log.info("Fetched all {} interactions", interactions.size());
        }

        return interactions.stream()
                .map(CustomerInteractionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public CustomerInteractionResponseDto updateInteraction(String interactionPublicId, AddInteractionRequestDto updatedInteraction) {
        log.info("Updating interaction with publicId: {}", interactionPublicId);

        CustomerInteraction interaction = interactionRepository.findByPublicId(interactionPublicId)
                .orElseThrow(() -> {
                    log.error("Interaction not found with publicId: {}", interactionPublicId);
                    return new EntityNotFoundException("Interaction not found with id: " + interactionPublicId);
                });

        interaction.setInteractionType(updatedInteraction.getInteractionType());
        interaction.setNotes(updatedInteraction.getNotes());
        interaction.setDate(updatedInteraction.getDate());

        if (updatedInteraction.getCustomerPublicId() != null) {
            Customer customer = customerService.findCustomerByPublicId(updatedInteraction.getCustomerPublicId());
            interaction.setCustomer(customer);
        }

        log.info("Interaction updated successfully with publicId: {}", interactionPublicId);
        return CustomerInteractionMapper.toDto(interaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public void deleteInteraction(String interactionPublicId) {
        log.info("Deleting interaction with publicId: {}", interactionPublicId);
        interactionRepository.deleteByPublicId(interactionPublicId);
        log.info("Interaction deleted successfully with publicId: {}", interactionPublicId);
    }
}
