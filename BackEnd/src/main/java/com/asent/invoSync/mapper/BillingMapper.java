package com.asent.invoSync.mapper;

import java.util.stream.Collectors;

import com.asent.invoSync.dto.BillingDTO;
import com.asent.invoSync.entity.Bill;

public class BillingMapper {
    public static BillingDTO toDTO(Bill b){
        if(b==null) return null;
        BillingDTO dto = new BillingDTO();
        dto.setId(b.getId());
        dto.setDrawingId(b.getDrawing()!=null?b.getDrawing().getId():null);
        dto.setQuotationId(b.getQuotation()!=null?b.getQuotation().getId():null);
        dto.setTotal(b.getTotal());
        dto.setRemainingAmount(b.getRemainingAmount());
        dto.setPdfUrl(b.getPdfUrl());
        if(b.getItems()!=null) dto.setItems(b.getItems().stream().map(ItemMapper::toDTO).collect(Collectors.toList()));
        if(b.getQuotation()!=null && b.getQuotation().getCustomer()!=null){
            dto.setCustomerId(b.getQuotation().getCustomer().getId());
            dto.setCustomerName(b.getQuotation().getCustomer().getName());
            dto.setCustomerEmail(b.getQuotation().getCustomer().getEmail());
        }
        return dto;
    }
}
