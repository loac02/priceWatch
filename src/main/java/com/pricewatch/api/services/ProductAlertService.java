package com.pricewatch.api.services;

import com.pricewatch.api.domain.relational.ProductAlert;
import com.pricewatch.api.repository.relational.ProductAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAlertService {

    private final ProductAlertRepository repository;

    public ProductAlertService(ProductAlertRepository repository) {
        this.repository = repository;
    }

    public ProductAlert saveAlert(ProductAlert alert) {
        return repository.save(alert);
    }

    public List<ProductAlert> getAllAlerts() {
        return repository.findAll();
    }
}
