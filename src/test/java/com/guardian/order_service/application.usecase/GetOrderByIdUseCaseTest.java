package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.domain.model.OrderStatus;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetOrderByIdUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderByIdUseCase getOrderByIdUseCase;

    @Test
    void shouldReturnOrderWhenFound() {
        UUID id = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), 2, OrderStatus.CREATED);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        Optional<Order> result = getOrderByIdUseCase.execute(id);
        assertTrue(result.isPresent());
        assertEquals(OrderStatus.CREATED, result.get().getStatus());
    }
    @Test
    void shouldReturnEmptyWhenNotFound () {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Order> result = getOrderByIdUseCase.execute(id);
        assertTrue(result.isEmpty());

    }
}
