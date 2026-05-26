package com.pricewatch.api.repository.nosql;

import com.pricewatch.api.domain.nosql.PriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceHistoryRepository extends MongoRepository<PriceHistory, String> {
    Optional<PriceHistory> findFirstByUrlProdutoOrderByDataColetaDesc(String urlProduto);
}