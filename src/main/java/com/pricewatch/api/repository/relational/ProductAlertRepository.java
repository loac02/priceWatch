package com.pricewatch.api.repository.relational;

import com.pricewatch.api.domain.relational.ProductAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAlertRepository extends JpaRepository<ProductAlert, Long> {
}