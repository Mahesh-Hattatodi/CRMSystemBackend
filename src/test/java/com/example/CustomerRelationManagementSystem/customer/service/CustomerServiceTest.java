package com.example.CustomerRelationManagementSystem.customer.service;

import com.example.CustomerRelationManagementSystem.customer.model.dto.AddCustomerRequestDto;
import com.example.CustomerRelationManagementSystem.customer.model.dto.CustomerResponseDto;
import com.example.CustomerRelationManagementSystem.customer.model.dto.CustomerUpdateRequest;
import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.customer.repository.CustomerRepository;
import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.Role;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.service.UserService;
import com.example.CustomerRelationManagementSystem.security.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthUtils authUtils;

    @InjectMocks
    private CustomerService customerService;

    private User salesRep;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        salesRep = new User();
        salesRep.setUsername("salesrep");
        salesRep.setPublicId("rep1");
        salesRep.setRoles(Set.of(new Role(1, RoleName.SALES_REPRESENTATIVE)));
    }

    @Test
    void testGetCustomer_Success() {
        Customer customer = new Customer();
        customer.setCustomerName("Alice");
        customer.setPublicId("cust1");

        when(authUtils.getLoggedInUser()).thenReturn(salesRep);
        when(customerRepository.existsByPublicIdAndSalesRepId("cust1", 1L)).thenReturn(true);
        when(customerRepository.findByPublicId("cust1")).thenReturn(Optional.of(customer));

        CustomerResponseDto dto = customerService.getCustomer("cust1");

        assertEquals("Alice", dto.getCustomerName());
    }

    @Test
    void testGetCustomer_NotFound() {
        when(authUtils.getLoggedInUser()).thenReturn(salesRep);
        when(customerRepository.existsByPublicIdAndSalesRepId("cust2", 1L)).thenReturn(true);
        when(customerRepository.findByPublicId("cust2")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomer("cust2"));
    }

    @Test
    void testUpdateCustomer_Success() {
        Customer customer = new Customer();
        customer.setCustomerName("Old Name");
        customer.setPublicId("cust1");

        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .customerName("New name")
                .email("new@email.com")
                .phone("123456")
                .address("New address")
                .build();

        when(customerRepository.findByPublicId("cust1")).thenReturn(Optional.of(customer));

        var updatedDto = customerService.updateCustomer("cust1", updateRequest);

        assertEquals("New name", updatedDto.getCustomerName());
        assertEquals("new@email.com", updatedDto.getEmail());
        assertEquals("New address", updatedDto.getAddress());
        assertEquals("123456", updatedDto.getPhone());
    }

    @Test
    void testFindCustomerByPublicId_NotFound() {
        when(customerRepository.findByPublicId("custX")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.findCustomerByPublicId("custX"));
    }
}
