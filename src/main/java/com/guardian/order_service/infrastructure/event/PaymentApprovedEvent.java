package com.guardian.order_service.infrastructure.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PaymentApprovedEvent {
    private UUID orderId;
    private UUID paymentId;
    private BigDecimal amount;
    private String currency;
}