package com.pricewatch.api.services;

import com.pricewatch.api.domain.nosql.PriceHistory;
import com.pricewatch.api.repository.nosql.PriceHistoryRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public void saveHistoryIfPriceChanged(PriceHistory history) {

        Optional<PriceHistory> ultimoHistorico = priceHistoryRepository.findFirstByUrlProdutoOrderByDataColetaDesc(history.getUrlProduto());

        if (ultimoHistorico.isPresent()) {
            if (ultimoHistorico.get().getPrecoColetado().compareTo(history.getPrecoColetado()) == 0) {
                System.out.println("[Mongo] Preço não mudou para " + history.getUrlProduto() + ". Gravação abortada.");
                return;
            }
        }

        // 3. Se mudou ou é o primeiro, salva no Atlas
        priceHistoryRepository.save(history);
        System.out.println("[Mongo] Novo preço detectado! Histórico salvo no Atlas.");
    }
}