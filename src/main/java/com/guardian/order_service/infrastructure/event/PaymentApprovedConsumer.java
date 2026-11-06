package com.guardian.order_service.infrastructure.event;

import com.guardian.order_service.application.usecase.ApproveOrderUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentApprovedConsumer {

    private final ApproveOrderUseCase approveOrderUseCase;

    public PaymentApprovedConsumer(ApproveOrderUseCase approveOrderUseCase) {
        this.approveOrderUseCase = approveOrderUseCase;
    }

    @KafkaListener(topics = "payment.approved")
    public void consume(PaymentApprovedEvent event) {
        log.info("Received payment.approved: orderId={}, paymentId={}",
                event.getOrderId(), event.getPaymentId());
        approveOrderUseCase.execute(event.getOrderId());
    }
}