package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.infrastructure.CatalogClient;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import com.guardian.order_service.web.dto.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrderWhenProductExists () {
    CreateOrderRequest request = new CreateOrderRequest(
            UUID.randomUUID(),
            2

    );
    when(catalogClient.productExists(any())).thenReturn(true);
        Order savedOrder = new Order(request.getProductId(), request.getQuantity(), "PENDING");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = createOrderUseCase.execute(request);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
    }
    @Test
    void shouldThrowExceptionWhenProductNotFound(){
    CreateOrderRequest request = new CreateOrderRequest(UUID.randomUUID(),2);
    when(catalogClient.productExists(any())).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () -> createOrderUseCase.execute(request));
    }


}
