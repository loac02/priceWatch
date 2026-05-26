package com.pricewatch.api.scheduler;

import com.pricewatch.api.config.RabbitMQConfig;
import com.pricewatch.api.domain.nosql.PriceHistory;
import com.pricewatch.api.domain.relational.ProductAlert;
import com.pricewatch.api.facade.app.IAppFacade;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Component
public class PriceScheduler {

    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();
    private final IAppFacade facade;

    public PriceScheduler(IAppFacade facade, RabbitTemplate rabbitTemplate) {
        this.facade = facade;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void checkPrices() {
        List<ProductAlert> alerts = facade.getAllAlerts();

        if (alerts.isEmpty()) {
            System.out.println("[Scheduler] Nenhum alerta cadastrado para monitorar.");
            return;
        }

        for (ProductAlert alert : alerts) {
            System.out.println("[Scheduler] Raspando preço real para: " + alert.getUrlProduto());

            BigDecimal currentPrice = facade.capturarPreco(alert.getUrlProduto());

            if (currentPrice == null) {
                continue;
            }

            System.out.println("[Scheduler] Preço REAL capturado: R$ " + currentPrice);

            PriceHistory history = new PriceHistory(alert.getUrlProduto(), currentPrice);

            facade.saveHistoryIfPriceChanged(history);

            if (currentPrice.compareTo(alert.getPrecoAlvo()) <= 0) {

                if (!alert.isNotificado()) {
                    System.out.println("🔥🔥 ALVO ATINGIDO! Enviando notificação única para a fila...");

                    String message = "Para: " + alert.getEmailUsuario() + " | O produto " + alert.getUrlProduto() + " baixou para R$ " + currentPrice;
                    rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_ALERTAS, message);

                    alert.setNotificado(true);
                    facade.saveAlert(alert);
                } else {
                    System.out.println("[Scheduler] Alvo continua atingido, mas o usuário já foi notificado anteriormente.");
                }

            } else {
                if (alert.isNotificado()) {
                    System.out.println("[Scheduler] O preço subiu novamente. Resetando flag de notificação para o futuro.");
                    alert.setNotificado(false);
                    facade.saveAlert(alert);
                }
            }
        }
    }
}