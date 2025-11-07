package com.asent.invoSync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asent.invoSync.entity.*;
import com.asent.invoSync.repository.*;
import java.util.*;

@Service
public class BillingService {

    @Autowired private QuotationRepository quotationRepository;
    @Autowired private BillingRepository billingRepository;
    @Autowired private ItemRepository itemRepository;

    /**
     * Create bill by copying items from quotation; returns created bill id
     */
    public Long createBillFromQuotation(Long quotationId) {
        Quotation q = quotationRepository.findByIdWithItems(quotationId).orElseThrow(() -> new RuntimeException("Quotation not found"));
        q.setStatus("Confirmed");
        quotationRepository.save(q);

        Bill bill = new Bill();
        bill.setDrawing(q.getDrawing());
        bill.setTotal(0.0);
        bill.setRemainingAmount(0.0);
        billingRepository.save(bill);

        double sum = 0.0;
        for(Item old : q.getItems()){
            Item ni = new Item();
            ni.setParticular(old.getParticular());
            ni.setQuantity(old.getQuantity());
            ni.setPrice(old.getPrice());
            ni.setTotal(old.getTotal());
            ni.setBilling(bill);
            itemRepository.save(ni);
            sum += (old.getTotal()!=null?old.getTotal():0);
        }
        bill.setTotal(sum);
        bill.setRemainingAmount(sum);
        billingRepository.save(bill);
        return bill.getId();
    }

    public Bill getBill(Long id){
        return billingRepository.findById(id).orElseThrow(()->new RuntimeException("Bill not found"));
    }
}
