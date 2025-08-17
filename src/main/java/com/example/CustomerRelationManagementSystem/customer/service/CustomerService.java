package com.example.CustomerRelationManagementSystem.customer.service;

import com.example.CustomerRelationManagementSystem.customer.model.CustomerMapper;
import com.example.CustomerRelationManagementSystem.customer.model.dto.AddCustomerRequestDto;
import com.example.CustomerRelationManagementSystem.customer.model.dto.CustomerResponseDto;
import com.example.CustomerRelationManagementSystem.customer.model.dto.CustomerUpdateRequest;
import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.customer.repository.CustomerRepository;
import com.example.CustomerRelationManagementSystem.security.model.RoleName;
import com.example.CustomerRelationManagementSystem.security.model.entity.User;
import com.example.CustomerRelationManagementSystem.security.service.UserService;
import com.example.CustomerRelationManagementSystem.security.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final AuthUtils authUtils;

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public void createCustomerRecord(AddCustomerRequestDto dto) {
        log.info("Creating customer record: {}", dto.getCustomerName());

        User assignedSalesRep = userService.findUserByPublicId(dto.getSalesRepPublicId());
        log.info("Assigned Sales Representative: {}", assignedSalesRep.getUsername());

        Customer customer = CustomerMapper.toEntity(dto, assignedSalesRep);
        customerRepository.save(customer);

        log.info("Customer record created successfully: {}", customer.getCustomerName());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public void deleteCustomerRecord(String customerPublicId) {
        log.info("Deleting customer record with publicId: {}", customerPublicId);
        customerRepository.deleteByPublicId(customerPublicId);
        log.info("Customer record deleted successfully: {}", customerPublicId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'DATA_ANALYST')")
    public List<CustomerResponseDto> getAllCustomers() {
        log.info("Fetching all customer records");

        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponseDto> customerResponseDtoList = new ArrayList<>();

        for (Customer customer : customers) {
            customerResponseDtoList.add(CustomerMapper.toDto(customer));
        }

        log.info("Fetched {} customer records", customerResponseDtoList.size());
        return customerResponseDtoList;
    }

    public CustomerResponseDto getCustomer(String customerPublicId) {
        log.info("Fetching customer with publicId: {}", customerPublicId);

        User loggedInUser = authUtils.getLoggedInUser();
        if (loggedInUser.hasRole(RoleName.SALES_REPRESENTATIVE) &&
                !customerRepository.existsByPublicIdAndSalesRepId(customerPublicId, loggedInUser.getId())) {
            log.warn("Unauthorized access attempt by sales rep: {}", loggedInUser.getUsername());
            throw new RuntimeException("This customer is not assigned to the sales representative with id " + loggedInUser.getPublicId());
        }

        Customer customer = customerRepository.findByPublicId(customerPublicId)
                .orElseThrow(() -> {
                    log.error("Customer record not found for publicId: {}", customerPublicId);
                    return new EntityNotFoundException("Customer record not found for id " + customerPublicId);
                });

        log.info("Customer record fetched successfully: {}", customer.getCustomerName());
        return CustomerMapper.toDto(customer);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public CustomerResponseDto updateCustomer(String customerPublicId, CustomerUpdateRequest updatedCustomer) {
        log.info("Updating customer record with publicId: {}", customerPublicId);

        Customer customer = customerRepository.findByPublicId(customerPublicId)
                .orElseThrow(() -> {
                    log.error("Customer record not found for publicId: {}", customerPublicId);
                    return new EntityNotFoundException("Customer record not found for id " + customerPublicId);
                });

        customer.setCustomerName(updatedCustomer.getCustomerName());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setAddress(updatedCustomer.getAddress());
        customer.setPhone(updatedCustomer.getPhone());

        log.info("Customer record updated successfully: {}", customer.getCustomerName());
        return CustomerMapper.toDto(customer);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public void reassignSalesRep(String oldSalesRepId, String newSalesRepId) {
        log.info("Reassigning customers from sales rep {} to {}", oldSalesRepId, newSalesRepId);

        User newRep = userService.findUserByPublicId(newSalesRepId);
        customerRepository.updateSalesRepForCustomer(oldSalesRepId, newRep);

        log.info("Customer reassignment completed from {} to {}", oldSalesRepId, newSalesRepId);
    }

    public Customer findCustomerByPublicId(String customerPublicId) {
        log.info("Finding customer by publicId: {}", customerPublicId);

        Customer customer = customerRepository.findByPublicId(customerPublicId)
                .orElseThrow(() -> {
                    log.error("Customer record not found for publicId: {}", customerPublicId);
                    return new EntityNotFoundException("Customer record not found for id " + customerPublicId);
                });

        log.info("Customer found: {}", customer.getCustomerName());
        return customer;
    }
}
