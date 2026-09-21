package com.guardian.order_service.infrastructure.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class OrderCreatedEvent {
    private UUID orderId;
    private BigDecimal amount;
    private String currency;
    private Instant createdAt;

    public OrderCreatedEvent (UUID orderId, BigDecimal amount, String currency, Instant createdAt) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = createdAt;
    }
    public UUID getOrderId() {
        return this.orderId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }
    public String getCurrency(){
        return this.currency;

    }
    public Instant getCreatedAt(){
        return this.createdAt;
    }
}
