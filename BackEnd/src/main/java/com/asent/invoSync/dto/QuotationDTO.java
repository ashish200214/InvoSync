package com.asent.invoSync.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * Flattened DTO to match the React form payload.
 * Contains customer fields directly (name, email, whatsAppNo) so backend
 * won't get NPE when frontend sends flat JSON.
 */
@Data
public class QuotationDTO {
    private Long id;

    // flattened customer fields (sent from frontend)
    private String name;
    private String email;
    private String whatsAppNo;

    // other quotation fields
    private Long drawingId;
    private LocalDateTime date;
    private Double total;
    private String status;
    private List<ItemDTO> items;
    private String initialRequirement;
}
