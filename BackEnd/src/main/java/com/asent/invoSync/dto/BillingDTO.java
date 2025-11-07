package com.asent.invoSync.dto;
import lombok.Data;
import java.util.*;

@Data
public class BillingDTO {
    private Long id;
    private Long drawingId;
    private Double total;
    private Double remainingAmount;
    private List<ItemDTO> items;
}
