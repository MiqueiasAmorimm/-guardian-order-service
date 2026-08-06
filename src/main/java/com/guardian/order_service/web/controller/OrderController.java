package com.guardian.order_service.web.controller;

import com.guardian.order_service.application.usecase.CreateOrderUseCase;
import com.guardian.order_service.application.usecase.GetOrderByIdUseCase;
import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.web.dto.CreateOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           GetOrderByIdUseCase getOrderByIdUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;

    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable UUID id) {
        Optional<Order> order = getOrderByIdUseCase.execute(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody CreateOrderRequest request) {
        Order order = createOrderUseCase.execute(request);
        return ResponseEntity.status(201).body(order);
    }

}
