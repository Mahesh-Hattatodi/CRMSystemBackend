package com.example.CustomerRelationManagementSystem.customer.controller;

import com.example.CustomerRelationManagementSystem.customer.model.dto.AddCustomerRequestDto;
import com.example.CustomerRelationManagementSystem.customer.model.dto.CustomerResponseDto;
import com.example.CustomerRelationManagementSystem.customer.model.dto.CustomerUpdateRequest;
import com.example.CustomerRelationManagementSystem.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody AddCustomerRequestDto dto) {
        customerService.createCustomerRecord(dto);
        return ResponseEntity.ok("Customer created successfully");
    }

    @DeleteMapping("/{customerPublicId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerPublicId) {
        customerService.deleteCustomerRecord(customerPublicId);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> customers = customerService.getAllCustomers();

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerPublicId}")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable String customerPublicId) {
        CustomerResponseDto customer = customerService.getCustomer(customerPublicId);

        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{customerPublicId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable String customerPublicId,
            @RequestBody CustomerUpdateRequest updatedCustomer
    ) {
        CustomerResponseDto customer = customerService.updateCustomer(customerPublicId, updatedCustomer);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/reassign")
    public ResponseEntity<String> reassignSalesRep(
            @RequestParam String oldSalesRepId,
            @RequestParam String newSalesRepId
    ) {
        customerService.reassignSalesRep(oldSalesRepId, newSalesRepId);
        return ResponseEntity.ok("Sales rep reassigned successfully");
    }
}
