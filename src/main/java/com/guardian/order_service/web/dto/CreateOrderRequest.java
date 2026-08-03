package com.guardian.order_service.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private UUID productId;

    @NotNull
    @Positive
    private Integer quantity;
}