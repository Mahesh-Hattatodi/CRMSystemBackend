package com.example.CustomerRelationManagementSystem.sales.service;

import com.example.CustomerRelationManagementSystem.customer.model.entity.Customer;
import com.example.CustomerRelationManagementSystem.customer.service.CustomerService;
import com.example.CustomerRelationManagementSystem.sales.model.SaleMapper;
import com.example.CustomerRelationManagementSystem.sales.model.dto.AddSaleRecordRequestDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SaleResponseDto;
import com.example.CustomerRelationManagementSystem.sales.model.entity.Sale;
import com.example.CustomerRelationManagementSystem.sales.repository.SaleRepository;
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
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerService customerService;
    private final AuthUtils authUtils;

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    public SaleResponseDto createSale(AddSaleRecordRequestDto dto) {
        log.info("Creating sale for customerPublicId: {}", dto.getCustomerPublicId());

        Customer customer = customerService.findCustomerByPublicId(dto.getCustomerPublicId());
        Sale sale = SaleMapper.toEntity(dto, customer);
        Sale savedSale = saleRepository.save(sale);

        log.info("Sale created successfully with publicId: {}", savedSale.getPublicId());
        return SaleMapper.toDto(savedSale);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    public SaleResponseDto getSale(String salePublicId) {
        log.info("Fetching sale with publicId: {}", salePublicId);

        Sale sale = saleRepository.findByPublicId(salePublicId)
                .orElseThrow(() -> {
                    log.error("Sale not found with publicId: {}", salePublicId);
                    return new EntityNotFoundException("Sale not found with id: " + salePublicId);
                });

        User loggedInUser = authUtils.getLoggedInUser();
        if (loggedInUser.hasRole(RoleName.SALES_REPRESENTATIVE) &&
                !sale.getCustomer().getSalesRep().getPublicId().equals(loggedInUser.getPublicId())) {
            log.warn("Unauthorized access attempt by sales rep {} for sale {}",
                    loggedInUser.getUsername(), salePublicId);
            throw new RuntimeException("You are not authorized to view this sale. This customer is not assigned to you.");
        }

        log.info("Sale fetched successfully with publicId: {}", salePublicId);
        return SaleMapper.toDto(sale);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'DATA_ANALYST')")
    public List<SaleResponseDto> getAllSales() {
        log.info("Fetching all sales records");

        List<SaleResponseDto> sales = saleRepository.findAll().stream()
                .map(SaleMapper::toDto)
                .collect(Collectors.toList());

        log.info("Fetched {} sales records", sales.size());
        return sales;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public void deleteSale(String salePublicId) {
        log.info("Deleting sale with publicId: {}", salePublicId);
        saleRepository.deleteByPublicId(salePublicId);
        log.info("Sale deleted successfully with publicId: {}", salePublicId);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    public SaleResponseDto updateSale(String salePublicId, AddSaleRecordRequestDto updatedSale) {
        log.info("Updating sale with publicId: {}", salePublicId);

        Sale sale = saleRepository.findByPublicId(salePublicId)
                .orElseThrow(() -> {
                    log.error("Sale not found with publicId: {}", salePublicId);
                    return new EntityNotFoundException("Sale not found with id: " + salePublicId);
                });

        sale.setProductName(updatedSale.getProductName());
        sale.setAmount(updatedSale.getAmount());
        sale.setDate(updatedSale.getDate());

        if (updatedSale.getCustomerPublicId() != null) {
            Customer customer = customerService.findCustomerByPublicId(updatedSale.getCustomerPublicId());
            sale.setCustomer(customer);
        }

        log.info("Sale updated successfully with publicId: {}", salePublicId);
        return SaleMapper.toDto(sale);
    }
}
