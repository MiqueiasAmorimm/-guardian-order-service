package com.guardian.order_service.infrastructure.event;

import com.guardian.order_service.application.usecase.UpdateOrderStatusUseCase;
import com.guardian.order_service.domain.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentApprovedConsumer {

    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    public PaymentApprovedConsumer(UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
    }

    @KafkaListener(topics = "payment.approved")
    public void consume(PaymentApprovedEvent event) {
        log.info("Received payment.approved: orderId={}, paymentId={}",
                event.getOrderId(), event.getPaymentId());
        updateOrderStatusUseCase.execute(event.getOrderId(), OrderStatus.APPROVED);
    }
}