package com.asent.invoSync.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.asent.invoSync.entity.*;
import com.asent.invoSync.repository.*;
import com.asent.invoSync.dto.QuotationDTO;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * QuotationService - provides methods for listing, fetching,
 * creating and sending quotations.
 *
 * This version expects QuotationDTO in the flattened shape:
 *  { name, email, whatsAppNo, initialRequirement, ... }
 */
@Service
public class QuotationService {

    @Autowired private QuotationRepository quotationRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private BillingRepository billRepository;
    @Autowired private S3Service s3Service;
    @Autowired private EmailService emailService;

    // Get all quotations (return flattened DTO list)
    public List<QuotationDTO> getAll() {
        List<Quotation> quotations = quotationRepository.findAll();
        return quotations.stream().map(q -> {
            QuotationDTO dto = new QuotationDTO();
            dto.setId(q.getId());
            dto.setInitialRequirement(q.getInitialRequirement());
            dto.setStatus(q.getStatus());
            dto.setDate(q.getDate());
            dto.setTotal(q.getTotal());
            if (q.getCustomer() != null) {
                dto.setName(q.getCustomer().getName());
                dto.setEmail(q.getCustomer().getEmail());
                dto.setWhatsAppNo(q.getCustomer().getWhatsAppNo());
            }
            // Optionally map items to DTOs if needed (keeping as-is)
            return dto;
        }).collect(Collectors.toList());
    }

    // Get single quotation by id (flattened DTO)
    public QuotationDTO getById(Long id) {
        Quotation q = quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found for id: " + id));
        QuotationDTO dto = new QuotationDTO();
        dto.setId(q.getId());
        dto.setInitialRequirement(q.getInitialRequirement());
        dto.setStatus(q.getStatus());
        dto.setDate(q.getDate());
        dto.setTotal(q.getTotal());
        if (q.getCustomer() != null) {
            dto.setName(q.getCustomer().getName());
            dto.setEmail(q.getCustomer().getEmail());
            dto.setWhatsAppNo(q.getCustomer().getWhatsAppNo());
        }
        // If you want to include items you can map them here
        return dto;
    }

    /**
     * Send quotation - uploads files and sends email (PDF + images).
     * Parameters: pdfFile (optional), drawingFile (optional), images (optional).
     *
     * NOTE: this method signature matches the controller usage in your project.
     */
    public String sendQuotation(Long id, MultipartFile pdfFile, MultipartFile drawingFile, List<MultipartFile> images)
            throws IOException {

        Quotation q = quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found for id: " + id));

        Customer customer = q.getCustomer();
        if (customer == null) throw new RuntimeException("Customer not found for quotation id: " + id);

        String whatsAppNo = customer.getWhatsAppNo() != null ? customer.getWhatsAppNo() : "unknown";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String ts = LocalDateTime.now().format(fmt);

        // Upload PDF (if present)
        String pdfUrl = null;
        if (pdfFile != null && !pdfFile.isEmpty()) {
            String pdfBase = "quotation_" + id + "_" + ts;
            pdfUrl = s3Service.uploadFileWithCustomerFolder(pdfFile, whatsAppNo, "pdf", pdfBase);
        }

        // Upload drawing (store but do not include in email)
        if (drawingFile != null && !drawingFile.isEmpty()) {
            String drawingBase = "quotation_" + id + "_drawing_" + ts;
            s3Service.uploadFileWithCustomerFolder(drawingFile, whatsAppNo, "drawings", drawingBase);
        }

        // Upload images
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                String imgBase = "quotation_" + id + "_img" + (i + 1) + "_" + ts;
                imageUrls.add(s3Service.uploadFileWithCustomerFolder(images.get(i), whatsAppNo, "images", imgBase));
            }
        }

        // Create bill if needed
        Bill bill = new Bill();
        bill.setDrawing(q.getDrawing());
        double sum = q.getItems() != null
                ? q.getItems().stream().mapToDouble(it -> it.getTotal() != null ? it.getTotal() : 0).sum()
                : 0;
        bill.setTotal(sum);
        bill.setRemainingAmount(sum);
        billRepository.save(bill);

        // Send email with PDF and images only
        String email = customer.getEmail();
        if (email != null && !email.isEmpty()) {
            List<String> links = new ArrayList<>();
            if (pdfUrl != null) links.add("Quotation PDF: " + pdfUrl);
            links.addAll(imageUrls);

            emailService.sendQuotationLinks(
                    email,
                    "Your Quotation",
                    "Please find your quotation and product images below:",
                    links
            );
        } else {
            // If no email present, still return success text but inform user
            return "Quotation processed but customer has no email registered.";
        }

        return "Quotation sent successfully to " + email;
    }

    // Create Quotation + Customer using flattened DTO
    public String createQuotationAndCustomer(QuotationDTO dto) {

        if (dto == null) {
            throw new RuntimeException("Request body is empty.");
        }

        // If frontend sent flat shape, use whatsAppNo from dto
        if (dto.getWhatsAppNo() == null || dto.getWhatsAppNo().isBlank()) {
            throw new RuntimeException("WhatsApp number is required.");
        }

        // Find existing customer by WhatsApp
        Customer customer = customerRepository.findByWhatsAppNo(dto.getWhatsAppNo());
        if (customer == null) {
            customer = new Customer();
            customer.setName(dto.getName());
            customer.setEmail(dto.getEmail());
            customer.setWhatsAppNo(dto.getWhatsAppNo());
            customer.setDate(LocalDateTime.now());
            customerRepository.save(customer);
        }

        Quotation quotation = new Quotation();
        quotation.setCustomer(customer);
        quotation.setInitialRequirement(dto.getInitialRequirement());
        quotation.setStatus("Pending");
        quotation.setDate(LocalDateTime.now());
        quotationRepository.save(quotation);

        return "Customer and quotation created successfully";
    }
}
