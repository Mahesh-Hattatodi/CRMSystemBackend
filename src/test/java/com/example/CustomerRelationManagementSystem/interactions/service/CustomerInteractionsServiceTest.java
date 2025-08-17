package com.example.CustomerRelationManagementSystem.interactions.service;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.customer.service.CustomerService;
import com.example.CustomerRelationManagementSystem.interactions.model.CustomerInteractionMapper;
import com.example.CustomerRelationManagementSystem.interactions.model.InteractionType;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.AddInteractionRequestDto;
import com.example.CustomerRelationManagementSystem.interactions.model.dto.CustomerInteractionResponseDto;
import com.example.CustomerRelationManagementSystem.interactions.model.entity.CustomerInteraction;
import com.example.CustomerRelationManagementSystem.interactions.repository.CustomerInteractionRepository;
import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerInteractionsServiceTest {

    @Mock
    private CustomerInteractionRepository interactionRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private AuthUtils authUtils;

    @InjectMocks
    private CustomerInteractionsService interactionsService;

    private User salesRep;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        salesRep = new User();
        salesRep.setPublicId("rep1");
        salesRep.setUsername("salesrep");

        customer = new Customer();
        customer.setPublicId("cust1");
        customer.setCustomerName("John Doe");
        customer.setSalesRep(salesRep);
    }

    @Test
    void testGetCustomerInteraction_Success() {
        CustomerInteraction interaction = new CustomerInteraction();
        interaction.setPublicId("int1");
        interaction.setCustomer(customer);

        when(interactionRepository.findByPublicId("int1")).thenReturn(Optional.of(interaction));
        when(authUtils.getLoggedInUser()).thenReturn(salesRep);

        CustomerInteractionResponseDto dto = interactionsService.getCustomerInteraction("int1");
        assertNotNull(dto);
    }

    @Test
    void testGetCustomerInteraction_NotFound() {
        when(interactionRepository.findByPublicId("intX")).thenReturn(Optional.empty());
        when(authUtils.getLoggedInUser()).thenReturn(salesRep);

        assertThrows(EntityNotFoundException.class,
                () -> interactionsService.getCustomerInteraction("intX"));
    }

    @Test
    void testUpdateInteraction_Success() {
        CustomerInteraction interaction = new CustomerInteraction();
        interaction.setPublicId("int1");
        interaction.setCustomer(customer);

        AddInteractionRequestDto updateDto = AddInteractionRequestDto.builder()
                .interactionType(InteractionType.EMAIL)
                .notes("Sent follow-up email")
                .date(LocalDate.now())
                .build();

        when(interactionRepository.findByPublicId("int1")).thenReturn(Optional.of(interaction));
        when(customerService.findCustomerByPublicId(anyString())).thenReturn(customer);

        CustomerInteractionResponseDto updated = interactionsService.updateInteraction("int1", updateDto);

        assertEquals(InteractionType.EMAIL, updated.getInteractionType());
        assertEquals("Sent follow-up email", updated.getNotes());
    }

    @Test
    void testDeleteInteraction_Success() {
        interactionsService.deleteInteraction("int1");
        verify(interactionRepository, times(1)).deleteByPublicId("int1");
    }

    @Test
    void testGetAllInteractions_AsAdmin() {
        // Set role as ADMIN
        salesRep.setRoles(Set.of(new Role(1, RoleName.ADMIN)));

        // Mock some interactions (can be all interactions in case of ADMIN)
        CustomerInteraction interaction1 = new CustomerInteraction();
        interaction1.setPublicId("int1");
        interaction1.setCustomer(customer);

        CustomerInteraction interaction2 = new CustomerInteraction();
        interaction2.setPublicId("int2");
        interaction2.setCustomer(customer);

        when(authUtils.getLoggedInUser()).thenReturn(salesRep);

        // For ADMIN, maybe fetch all interactions
        when(interactionRepository.findAll()).thenReturn(List.of(interaction1, interaction2));

        List<CustomerInteractionResponseDto> list = interactionsService.getAllInteractions();

        verify(interactionRepository, times(1)).findAll();

        assertEquals(2, list.size());
    }

    @Test
    void testGetAllInteractions_AsSalesRep() {
        salesRep.setRoles(Set.of(new Role(3, RoleName.SALES_REPRESENTATIVE)));

        CustomerInteraction interaction1 = new CustomerInteraction();
        interaction1.setPublicId("int1");
        interaction1.setCustomer(customer);

        CustomerInteraction interaction2 = new CustomerInteraction();
        interaction2.setPublicId("int2");
        interaction2.setCustomer(customer);

        when(authUtils.getLoggedInUser()).thenReturn(salesRep);

        when(interactionRepository.findByCustomer_SalesRep_PublicId("rep1"))
                .thenReturn(List.of(interaction1, interaction2));

        List<CustomerInteractionResponseDto> list = interactionsService.getAllInteractions();

        verify(interactionRepository, times(1))
                .findByCustomer_SalesRep_PublicId("rep1");

        assertEquals(2, list.size());
    }

}
