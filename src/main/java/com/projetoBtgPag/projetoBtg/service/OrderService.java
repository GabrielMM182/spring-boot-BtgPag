package com.projetoBtgPag.projetoBtg.service;

import com.projetoBtgPag.projetoBtg.entity.OrderEntity;
import com.projetoBtgPag.projetoBtg.entity.OrderItem;
import com.projetoBtgPag.projetoBtg.listener.dto.OrderCreatedEventDto;
import com.projetoBtgPag.projetoBtg.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderCreatedEventDto eventDto) {
        var entity = new OrderEntity();

        entity.setOrderId(eventDto.orderId());
        entity.setCustomerId(eventDto.customerId());

        entity.setItems(getOrderItems(eventDto));
        entity.setTotal(getTotalItems(eventDto));

        orderRepository.save(entity);
    }

    private static BigDecimal getTotalItems(OrderCreatedEventDto eventDto) {
        return eventDto.itens().stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add) //Metodo reduce fica encarregado de fazer essa soma de quantidade * valor
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEventDto eventDto) {
        return eventDto.itens().stream()
                .map(i -> new OrderItem(i.product(), i.quantity(), i.price()))
                .toList();
    }


}
