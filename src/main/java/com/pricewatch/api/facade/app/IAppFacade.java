package com.pricewatch.api.facade.app;

import com.pricewatch.api.domain.nosql.PriceHistory;
import com.pricewatch.api.domain.relational.ProductAlert;
import com.pricewatch.api.facade.IFacade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IAppFacade extends IFacade {

    public ProductAlert saveAlert(ProductAlert alert);

    public List<ProductAlert> getAllAlerts();

    public void saveHistoryIfPriceChanged(PriceHistory history) ;

    public BigDecimal capturarPreco(String url);
}
