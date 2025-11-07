package com.asent.invoSync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.asent.invoSync.entity.Quotation;
import java.util.Optional;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    // ✅ Fix lazy loading: fetch customer with quotation
    @Query("SELECT q FROM Quotation q JOIN FETCH q.customer WHERE q.id = :id")
    Optional<Quotation> findByIdWithCustomer(@Param("id") Long id);
}
