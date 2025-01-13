package com.projetoBtgPag.projetoBtg.repository;

import com.projetoBtgPag.projetoBtg.entity.OrderEntity;
import com.projetoBtgPag.projetoBtg.listener.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {

    Page<OrderEntity> findAllByCustomerId(Long customerId, PageRequest pageRequest);
}
