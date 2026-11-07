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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApproveOrderUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ApproveOrderUseCase approveOrderUseCase;

    @Test
    void shouldIgnoreWhenAlreadyApproved() {
        UUID id = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), 2, OrderStatus.CREATED);
        order.updateStatus(OrderStatus.AWAITING_PAYMENT);
        order.updateStatus(OrderStatus.APPROVED);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        approveOrderUseCase.execute(id);

        verify(orderRepository, never()).save(any(Order.class));
    }
    @Test
    void shouldApproveWhenAwaitingPayment() {
        UUID id = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), 2, OrderStatus.CREATED);
        order.updateStatus(OrderStatus.AWAITING_PAYMENT);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        approveOrderUseCase.execute(id);

        assertEquals(OrderStatus.APPROVED, order.getStatus());
        verify(orderRepository).save(order);
    }
}
