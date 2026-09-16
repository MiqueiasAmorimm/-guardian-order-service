package com.guardian.order_service.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private static final OrderStatusTransitionValidator TRANSITION_VALIDATOR = new OrderStatusTransitionValidator();

    protected Order() {
        // for JPA
    }

    public Order(UUID productId, Integer quantity, OrderStatus status) {
        validateProductId(productId);
        validateQuantity(quantity);
        validateStatus(status);

        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateStatus(OrderStatus newStatus) {
        validateStatus(newStatus);
        if (!TRANSITION_VALIDATOR.canTransition(this.status, newStatus)) {
            throw new IllegalArgumentException(
                    "Cannot transition order from " + this.status + " to " + newStatus);
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    private void validateProductId(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId cannot be null");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }

    private void validateStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
    }
}