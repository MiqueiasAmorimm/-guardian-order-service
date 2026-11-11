package com.guardian.order_service.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderStatusTransitionValidatorTest {

    private final OrderStatusTransitionValidator validator = new OrderStatusTransitionValidator();

    @Test
    void shouldAllowCreatedToAwaitingPayment() {
        assertTrue(validator.canTransition(OrderStatus.CREATED, OrderStatus.AWAITING_PAYMENT));
    }

    @Test
    void shouldAllowAwaitingPaymentToApproved() {
        assertTrue(validator.canTransition(OrderStatus.AWAITING_PAYMENT, OrderStatus.APPROVED));
    }

    @Test
    void shouldRefuseCreatedToApproved() {
        assertFalse(validator.canTransition(OrderStatus.CREATED, OrderStatus.APPROVED));
    }

}