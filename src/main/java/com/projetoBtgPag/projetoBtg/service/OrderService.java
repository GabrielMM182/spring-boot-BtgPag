package com.projetoBtgPag.projetoBtg.service;

import org.bson.Document;
import com.projetoBtgPag.projetoBtg.entity.OrderEntity;
import com.projetoBtgPag.projetoBtg.entity.OrderItem;
import com.projetoBtgPag.projetoBtg.listener.dto.OrderCreatedEventDto;
import com.projetoBtgPag.projetoBtg.listener.dto.OrderResponse;
import com.projetoBtgPag.projetoBtg.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;


    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;

    }

    public void save(OrderCreatedEventDto eventDto) {
        var entity = new OrderEntity();

        entity.setOrderId(eventDto.orderId());
        entity.setCustomerId(eventDto.customerId());

        entity.setItems(getOrderItems(eventDto));
        entity.setTotal(getTotalItems(eventDto));

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllCustomerId(Long customerId, PageRequest pageRequest) {
        var orders =  orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    public BigDecimal calculateAverageSpending(Long customerId) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("customerId").is(customerId)),
                Aggregation.group()
                        .sum("total").as("total")
                        .count().as("count")
                );
        var result = mongoTemplate.aggregate(aggregation, "tb_orders", Document.class);
        if (result.getUniqueMappedResult() == null) {
            return BigDecimal.ZERO;
        }
        Document doc = result.getUniqueMappedResult();
        BigDecimal total = new BigDecimal(doc.get("total").toString());
        long count = Long.parseLong(doc.get("count").toString());

        return count > 0 ? total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
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
