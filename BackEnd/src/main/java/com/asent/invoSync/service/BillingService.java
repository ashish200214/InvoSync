package com.asent.invoSync.service;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.asent.invoSync.repository.BillingRepository;
import com.asent.invoSync.dto.BillingDTO;
import com.asent.invoSync.mapper.BillingMapper;

@Service
public class BillingService {
    @Autowired private BillingRepository billingRepository;
    public List<BillingDTO> getAll() {
        return billingRepository.findAll().stream().map(BillingMapper::toDTO).toList();
    }
}
