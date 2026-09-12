package com.guardian.order_service.domain.model;

import java.util.Map;
import java.util.Set;

public class OrderStatusTransitionValidator {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.CREATED, Set.of(OrderStatus.AWAITING_PAYMENT, OrderStatus.CANCELED),
            OrderStatus.AWAITING_PAYMENT, Set.of(OrderStatus.APPROVED, OrderStatus.REJECTED, OrderStatus.CANCELED),
            OrderStatus.APPROVED, Set.of(),
            OrderStatus.REJECTED, Set.of(),
            OrderStatus.CANCELED, Set.of()
    );

    public boolean canTransition(OrderStatus current, OrderStatus target) {
        Set<OrderStatus> allowedTargets = ALLOWED_TRANSITIONS.get(current);
        return allowedTargets != null && allowedTargets.contains(target);
    }
}