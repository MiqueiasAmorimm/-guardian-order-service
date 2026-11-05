package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.exception.ResourceNotFoundException;
import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.domain.model.OrderStatus;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ApproveOrderUseCase {

    private final OrderRepository orderRepository;

    public ApproveOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void execute(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.APPROVED) {
            log.warn("Order {} is already approved, ignoring duplicate event", orderId);
            return;
        }

        order.updateStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
    }
}