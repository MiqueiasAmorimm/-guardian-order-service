package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllOrdersUseCase {

    private final OrderRepository orderRepository;

    public GetAllOrdersUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<Order> execute() {
        return orderRepository.findAll();
    }
}
