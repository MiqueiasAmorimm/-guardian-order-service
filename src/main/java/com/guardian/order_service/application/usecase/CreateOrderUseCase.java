package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.domain.model.OrderStatus;
import com.guardian.order_service.infrastructure.CatalogClient;
import com.guardian.order_service.infrastructure.ProductInfo;
import com.guardian.order_service.infrastructure.event.OrderCreatedEvent;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import com.guardian.order_service.web.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;

@Service
public class CreateOrderUseCase {
    private final CatalogClient catalogClient;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public CreateOrderUseCase(CatalogClient catalogClient, OrderRepository orderRepository, KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.catalogClient = catalogClient;
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Order execute(CreateOrderRequest request) {
        ProductInfo product = catalogClient.getProduct(request.getProductId());
        Order order = new Order(request.getProductId(), request.getQuantity(), OrderStatus.CREATED);
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), product.getPrice(), "BRL", Instant.now());

        kafkaTemplate.send("order.created", event);
        return savedOrder;
    }
}