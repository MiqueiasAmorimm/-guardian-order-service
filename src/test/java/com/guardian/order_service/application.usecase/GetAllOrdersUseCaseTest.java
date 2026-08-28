package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllOrdersUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetAllOrdersUseCase getAllOrdersUseCase;

    @Test
    void shouldReturnAllOrders() {
        List<Order> orders = List.of(
                new Order(UUID.randomUUID(), 2, "PENDING"),
                new Order(UUID.randomUUID(), 1, "CONFIRMED")
        );
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = getAllOrdersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}