package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderUseCase {
    private final OrderRepository orderRepository;

    public CreateOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute() {

    }
}