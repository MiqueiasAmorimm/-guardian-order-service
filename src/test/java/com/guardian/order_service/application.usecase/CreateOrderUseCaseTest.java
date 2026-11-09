package com.guardian.order_service.application.usecase;

import com.guardian.order_service.domain.model.Order;
import com.guardian.order_service.domain.model.OrderStatus;
import com.guardian.order_service.infrastructure.CatalogClient;
import com.guardian.order_service.infrastructure.ProductInfo;
import com.guardian.order_service.infrastructure.event.OrderCreatedEvent;
import com.guardian.order_service.infrastructure.repository.OrderRepository;
import com.guardian.order_service.web.dto.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogClient catalogClient;

    @Mock
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrderWhenProductExists() {
        CreateOrderRequest request = new CreateOrderRequest(
                UUID.randomUUID(),
                2
        );

        ProductInfo productInfo = new ProductInfo();
        productInfo.setPrice(BigDecimal.valueOf(100));
        when(catalogClient.getProduct(any())).thenReturn(productInfo);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = createOrderUseCase.execute(request);

        assertNotNull(result);
        assertEquals(OrderStatus.AWAITING_PAYMENT, result.getStatus());
    }
    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        CreateOrderRequest request = new CreateOrderRequest(UUID.randomUUID(), 2);
        when(catalogClient.getProduct(any())).thenThrow(new IllegalArgumentException("Product not found"));

        assertThrows(IllegalArgumentException.class, () -> createOrderUseCase.execute(request));
    }

    @Test
    void shouldPublishOrderTotalAsPriceTimesQuantity() {
        CreateOrderRequest request = new CreateOrderRequest(UUID.randomUUID(), 3);
        ProductInfo product = new ProductInfo();
        product.setPrice(new BigDecimal("19.90"));
        when(catalogClient.getProduct(any())).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        createOrderUseCase.execute(request);

        ArgumentCaptor<OrderCreatedEvent> captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(kafkaTemplate).send(eq("order.created"), captor.capture());
        OrderCreatedEvent event = captor.getValue();

        assertEquals(new BigDecimal("59.70"), event.getAmount());
        assertEquals("BRL", event.getCurrency());
    }
}