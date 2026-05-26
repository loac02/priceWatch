package com.pricewatch.api.domain.nosql;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "price_history")
public class PriceHistory {

    @Id
    private String id;

    private String urlProduto;
    private BigDecimal precoColetado;

    @Indexed(expireAfter = "180d")
    private LocalDateTime dataColeta;

    public PriceHistory() {
        this.dataColeta = LocalDateTime.now();
    }

    public PriceHistory(String urlProduto, BigDecimal precoColetado) {
        this.urlProduto = urlProduto;
        this.precoColetado = precoColetado;
        this.dataColeta = LocalDateTime.now();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlProduto() {
        return urlProduto;
    }

    public void setUrlProduto(String urlProduto) {
        this.urlProduto = urlProduto;
    }

    public BigDecimal getPrecoColetado() {
        return precoColetado;
    }

    public void setPrecoColetado(BigDecimal precoColetado) {
        this.precoColetado = precoColetado;
    }

    public LocalDateTime getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDateTime dataColeta) {
        this.dataColeta = dataColeta;
    }
}