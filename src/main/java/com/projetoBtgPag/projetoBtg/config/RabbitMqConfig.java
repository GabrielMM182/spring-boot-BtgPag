package com.projetoBtgPag.projetoBtg.config;

import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ORDER_CREATED_QUEUE = "btg-pactual-order-created";
    public static final String DEAD_LETTER_QUEUE = "btg-pactual-order-created-dlq";
    public static final String DEAD_LETTER_EXCHANGE = "btg-pactual-dlx";

    public static final String TEST_QUEUE = "btg-pactual-order-test";



    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Declarable orderCreatedQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Declarable testQueue() {
        return new Queue(TEST_QUEUE);
    }

    @Bean
    public Declarable deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Declarable deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

}
