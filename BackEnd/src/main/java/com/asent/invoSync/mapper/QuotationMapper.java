package com.asent.invoSync.mapper;

import com.asent.invoSync.entity.Quotation;
import com.asent.invoSync.entity.Customer;
import com.asent.invoSync.dto.QuotationDTO;
import com.asent.invoSync.dto.CustomerDTO;

public class QuotationMapper {
    public static QuotationDTO toDTO(Quotation quotation) {
        if (quotation == null) return null;

        QuotationDTO dto = new QuotationDTO();
        dto.setId(quotation.getId());
        dto.setInitialRequirement(quotation.getInitialRequirement());
        dto.setDate(quotation.getDate());
        dto.setTotal(quotation.getTotal());
        dto.setStatus(quotation.getStatus());

        // ✅ Map Customer -> CustomerDTO
        Customer customer = quotation.getCustomer();
        if (customer != null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(customer.getId());
            customerDTO.setName(customer.getName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setWhatsAppNo(customer.getWhatsAppNo());
        }

        return dto;
    }
    
}
