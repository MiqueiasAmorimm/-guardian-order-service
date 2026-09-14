package com.guardian.order_service.web.controller;

import com.guardian.order_service.application.usecase.CreateOrderUseCase;
import com.guardian.order_service.application.usecase.GetAllOrdersUseCase;
import com.guardian.order_service.application.usecase.GetOrderByIdUseCase;
import com.guardian.order_service.application.usecase.UpdateOrderStatusUseCase;
import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.domain.model.OrderStatus;
import com.guardian.order_service.web.dto.CreateOrderRequest;
import com.guardian.order_service.web.dto.UpdateOrderStatusRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           GetOrderByIdUseCase getOrderByIdUseCase,
                           UpdateOrderStatusUseCase updateOrderStatusUseCase,
                           GetAllOrdersUseCase getAllOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable UUID id,
                                              @Valid @RequestBody UpdateOrderStatusRequest request) {
        Order order = updateOrderStatusUseCase.execute(id, OrderStatus.valueOf(request.getStatus()));
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public Page<Order> findAll(Pageable pageable) {
       return getAllOrdersUseCase.execute(pageable);

    }

}
