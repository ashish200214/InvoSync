package com.asent.invoSync.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.asent.invoSync.service.BillingService;
import com.asent.invoSync.dto.BillingDTO;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:5173")
public class BillController {
    @Autowired 
    private BillingService service;
    @GetMapping
    public List<BillingDTO> getAll() { return service.getAll(); }
}
