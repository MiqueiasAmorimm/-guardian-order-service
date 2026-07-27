package com.guardian.order_service.web.controller;

import com.guardian.order_service.application.usecase.CreateOrderUseCase;
import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.web.dto.CreateOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;


    public OrderController  (CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;

    }

    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody CreateOrderRequest request) {
        Order order = createOrderUseCase.execute(request);
        return ResponseEntity.status(201).body(order);
    }
}
