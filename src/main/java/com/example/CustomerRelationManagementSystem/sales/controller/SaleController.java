package com.example.CustomerRelationManagementSystem.sales.controller;

import com.example.CustomerRelationManagementSystem.sales.model.dto.AddSaleRecordRequestDto;
import com.example.CustomerRelationManagementSystem.sales.model.dto.SaleResponseDto;
import com.example.CustomerRelationManagementSystem.sales.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponseDto> createSale(@RequestBody AddSaleRecordRequestDto dto) {
        SaleResponseDto createdSale = saleService.createSale(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @GetMapping("/{salePublicId}")
    public ResponseEntity<SaleResponseDto> getSale(@PathVariable String salePublicId) {
        SaleResponseDto sale = saleService.getSale(salePublicId);
        return ResponseEntity.ok(sale);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDto>> getAllSales() {
        List<SaleResponseDto> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @PutMapping("/{salePublicId}")
    public ResponseEntity<SaleResponseDto> updateSale(
            @PathVariable String salePublicId,
            @RequestBody AddSaleRecordRequestDto updatedSale
    ) {
        SaleResponseDto sale = saleService.updateSale(salePublicId, updatedSale);
        return ResponseEntity.ok(sale);
    }

    @DeleteMapping("/{salePublicId}")
    public ResponseEntity<Void> deleteSale(@PathVariable String salePublicId) {
        saleService.deleteSale(salePublicId);
        return ResponseEntity.noContent().build();
    }
}
