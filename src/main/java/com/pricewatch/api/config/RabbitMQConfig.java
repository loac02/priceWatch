package com.pricewatch.api.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_ALERTAS = "fila.alertas-preco";

    @Bean
    public Queue queue() {
        // Cria uma fila durável (não se perde se o RabbitMQ reiniciar)
        return new Queue(QUEUE_ALERTAS, true);
    }
}