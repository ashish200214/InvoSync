package com.asent.invoSync.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;

import com.asent.invoSync.service.QuotationService;
import com.asent.invoSync.dto.QuotationDTO;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
@CrossOrigin(origins = "http://localhost:5173")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @GetMapping("/")
    public List<QuotationDTO> getAll() {
        return quotationService.getAll();
    }

    @GetMapping("/{id}")
    public QuotationDTO getById(@PathVariable Long id) {
        return quotationService.getById(id);
    }

    /**
     * Endpoint to receive generated PDF + drawing + images.
     * Frontend sends:
     *  - quotationPdf (multipart file) OR "quotationPdf" could be named according to frontend
     *  - drawingFile (optional)
     *  - images (optional list)
     *
     * NOTE: adapt request part names to match your frontend formData keys.
     */
    @PostMapping("/{quotationId}/send")
    public ResponseEntity<String> sendQuotation(
            @PathVariable Long quotationId,
            @RequestPart(value = "quotationPdf", required = false) MultipartFile quotationPdf,
            @RequestPart(value = "drawingFile", required = false) MultipartFile drawingFile,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        try {
            String res = quotationService.sendQuotation(quotationId, quotationPdf, drawingFile, images);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/saveQuotation")
    public ResponseEntity<String> saveQuotation(@RequestBody QuotationDTO dto) {
        try {
            String res = quotationService.createQuotationAndCustomer(dto);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
