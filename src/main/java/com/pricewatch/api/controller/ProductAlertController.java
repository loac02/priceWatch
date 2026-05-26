package com.pricewatch.api.controller;

import com.pricewatch.api.domain.relational.ProductAlert;
import com.pricewatch.api.facade.app.IAppFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class ProductAlertController {

    private final IAppFacade facade;

    public ProductAlertController(IAppFacade facade) {
        this.facade = facade;
    }

    // Endpoint para Criar Alerta (POST)
    @PostMapping
    public ResponseEntity<ProductAlert> createAlert(@Valid @RequestBody ProductAlert alert) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facade.saveAlert(alert));
    }

    // Endpoint para Listar todos os Alertas (GET)
    @GetMapping
    public ResponseEntity<List<ProductAlert>> getAllAlerts() {
        return ResponseEntity.ok(facade.getAllAlerts());
    }
}