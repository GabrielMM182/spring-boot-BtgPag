package com.projetoBtgPag.projetoBtg.repository;

import com.projetoBtgPag.projetoBtg.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {

}
