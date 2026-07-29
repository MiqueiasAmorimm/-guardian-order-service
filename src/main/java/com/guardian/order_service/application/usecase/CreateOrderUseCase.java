package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.infrastructure.CatalogClient;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import com.guardian.order_service.web.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderUseCase {
    private final CatalogClient catalogClient;
    private final OrderRepository orderRepository;

    public CreateOrderUseCase(CatalogClient catalogClient, OrderRepository orderRepository) {
        this.catalogClient = catalogClient;
        this.orderRepository = orderRepository;
    }

    public Order execute(CreateOrderRequest request)  {
        if (!catalogClient.productExists(request.getProductId())){
            throw new IllegalArgumentException("Product not found: " + request.getProductId());
        }
        Order order = new Order(request.getProductId(), request.getQuantity(), "PENDING");
        return orderRepository.save(order);
    }
}