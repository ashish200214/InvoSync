package com.asent.invoSync.dto;

import java.util.List;

import lombok.Data;

@Data
public class BillingDTO {
    private Long id;
    private Long drawingId;
    private Long quotationId;            // new
    private Double total;
    private Double remainingAmount;
    private String pdfUrl;               // new
    private List<ItemDTO> items;
    // optional: customer fields
    private Long customerId;
    private String customerName;
    private String customerEmail;
}
