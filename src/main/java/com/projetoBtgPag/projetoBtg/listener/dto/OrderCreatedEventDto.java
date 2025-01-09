package com.projetoBtgPag.projetoBtg.listener.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEventDto(Long orderId,
                                   Long customerId,

                                   BigDecimal total,
                                   List<OrderItemEventDTO> itens) {
}
