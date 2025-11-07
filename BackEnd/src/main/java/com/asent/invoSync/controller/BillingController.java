package com.asent.invoSync.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import com.asent.invoSync.service.BillingService;
import com.asent.invoSync.mapper.BillingMapper;
import com.asent.invoSync.dto.BillingDTO;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class BillingController {

    @Autowired private BillingService billingService;

    // Called when user clicks Confirm Order -> returns billId
    @PostMapping("/confirmQuotation/{quotationId}")
    public ResponseEntity<Long> confirmQuotation(@PathVariable Long quotationId){
        Long billId = billingService.createBillFromQuotation(quotationId);
        return ResponseEntity.ok(billId);
    }

    // Return bill DTO (for billing page)
    @GetMapping("/api/billing/{billId}")
    public ResponseEntity<BillingDTO> getBill(@PathVariable Long billId){
        var bill = billingService.getBill(billId);
        return ResponseEntity.ok(BillingMapper.toDTO(bill));
    }
}
