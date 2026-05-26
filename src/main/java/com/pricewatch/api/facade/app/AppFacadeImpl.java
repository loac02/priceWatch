package com.pricewatch.api.facade.app;

import com.pricewatch.api.domain.nosql.PriceHistory;
import com.pricewatch.api.domain.relational.ProductAlert;
import com.pricewatch.api.facade.FacadeImpl;
import com.pricewatch.api.repository.nosql.PriceHistoryRepository;
import com.pricewatch.api.services.PriceHistoryService;
import com.pricewatch.api.services.ProductAlertService;
import com.pricewatch.api.services.ScraperService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class AppFacadeImpl extends FacadeImpl implements IAppFacade{
    public AppFacadeImpl(ApplicationContext context) {
        super(context);
    }

    public ProductAlert saveAlert(ProductAlert alert) {
        return executaService(ProductAlertService.class, "saveAlert", alert);
    }

    public List<ProductAlert> getAllAlerts() {
        return executaService(ProductAlertService.class, "getAllAlerts");
    }

    public void saveHistoryIfPriceChanged(PriceHistory history) {
        executaService(PriceHistoryService.class, "saveHistoryIfPriceChanged", history);
    }

    public BigDecimal capturarPreco(String url) {
        return executaService(ScraperService.class, "capturarPreco", url);
    }
}
