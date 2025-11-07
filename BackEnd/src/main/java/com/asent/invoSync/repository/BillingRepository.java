package com.asent.invoSync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asent.invoSync.entity.Bill;

public interface BillingRepository extends JpaRepository<Bill, Long> {

}
