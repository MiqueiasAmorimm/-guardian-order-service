package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateOrderStatusUseCase {
    private final OrderRepository orderRepository;

    public UpdateOrderStatusUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public Order execute (UUID id, String newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " +id ));
        order.updateStatus(newStatus);
       return orderRepository.save(order);
    }

}
