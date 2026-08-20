package com.guardian.order_service.application.usecase;


import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateOrderStatusUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Test
    void shouldUpdateOrderStatusSuccessfully () {
    UUID id = UUID.randomUUID();
    String newStatus = "CONFIRMED";
    Order order = new Order(UUID.randomUUID(), 2, "PENDING");

    when(orderRepository.findById(id)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    Order result = updateOrderStatusUseCase.execute(id, newStatus);

    assertNotNull(result);
    assertEquals("CONFIRMED", result.getStatus());

    }
    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
    UUID id = UUID.randomUUID();
    when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> updateOrderStatusUseCase.execute(id, "CONFIRMED"));

    }

}
