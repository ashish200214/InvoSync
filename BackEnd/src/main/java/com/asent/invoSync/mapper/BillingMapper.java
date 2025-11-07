package com.asent.invoSync.mapper;
import com.asent.invoSync.entity.Bill;
import com.asent.invoSync.dto.BillingDTO;
import java.util.stream.Collectors;

public class BillingMapper {
    public static BillingDTO toDTO(Bill b){
        if(b==null) return null;
        BillingDTO dto = new BillingDTO();
        dto.setId(b.getId());
        dto.setDrawingId(b.getDrawing()!=null?b.getDrawing().getId():null);
        dto.setTotal(b.getTotal());
        dto.setRemainingAmount(b.getRemainingAmount());
        if(b.getItems()!=null) dto.setItems(b.getItems().stream().map(ItemMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }
}
