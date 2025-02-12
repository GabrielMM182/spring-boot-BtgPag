package com.projetoBtgPag.projetoBtg.controller;

import com.projetoBtgPag.projetoBtg.listener.dto.ApiResponse;
import com.projetoBtgPag.projetoBtg.listener.dto.OrderResponse;
import com.projetoBtgPag.projetoBtg.listener.dto.PaginationResponse;
import com.projetoBtgPag.projetoBtg.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        var pageResponse = orderService.findAllCustomerId(customerId, PageRequest.of(page, pageSize));
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);
        var averageSpending = orderService.calculateAverageSpending(customerId);


        return ResponseEntity.ok(new ApiResponse<>(
                Map.of(
                        "totalOnOrders", totalOnOrders,
                        "averageSpending", averageSpending

                ),
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
        ));
    }
}
