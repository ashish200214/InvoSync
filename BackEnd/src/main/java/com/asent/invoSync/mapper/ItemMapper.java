package com.asent.invoSync.mapper;
import com.asent.invoSync.entity.Item;
import com.asent.invoSync.dto.ItemDTO;

public class ItemMapper {
    public static ItemDTO toDTO(Item i){
        if(i==null) return null;
        ItemDTO dto = new ItemDTO();
        dto.setId(i.getId());
        dto.setQuantity(i.getQuantity());
        dto.setParticular(i.getParticular());
        dto.setPrice(i.getPrice());
        dto.setTotal(i.getTotal());
        dto.setQuotationId(i.getQuotation()!=null?i.getQuotation().getId():null);
        dto.setBillingId(i.getBilling()!=null?i.getBilling().getId():null);
        return dto;
    }
}
