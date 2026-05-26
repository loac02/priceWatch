package com.pricewatch.api.consumer;

import com.pricewatch.api.config.RabbitMQConfig;
import com.pricewatch.api.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ALERTAS)
    public void escutarFilaDeAlertas(String mensagem) {
        System.out.println("[RabbitMQ] Mensagem recebida da fila: " + mensagem);

        try {
            String emailDestinatario = mensagem.substring(mensagem.indexOf("Para: ") + 6, mensagem.indexOf(" |")).trim();

            String corpoEmail = mensagem.substring(mensagem.indexOf("|") + 1).trim();

            emailService.enviarEmailSimples(
                    emailDestinatario,
                    "📉 Alerta de Preço Atingido! - PriceWatch",
                    corpoEmail
            );

        } catch (Exception e) {
            System.err.println("[RabbitMQ Erro] Erro ao processar mensagem da fila: " + e.getMessage());
        }
    }
}