package com.projetoBtgPag.projetoBtg.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projetoBtgPag.projetoBtg.listener.dto.OrderCreatedEventDto;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.projetoBtgPag.projetoBtg.config.RabbitMqConfig.*;

@Component
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);
    private final RabbitTemplate rabbitTemplate;

    public OrderCreatedListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEventDto> message){
        try {
            OrderCreatedEventDto payload = message.getPayload();
            //logger.info("Message consumes: {}", message);
            logger.info("Processing order: {}", payload);
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
            rabbitTemplate.convertAndSend(DEAD_LETTER_EXCHANGE, DEAD_LETTER_QUEUE, message);
        }
    }
}
