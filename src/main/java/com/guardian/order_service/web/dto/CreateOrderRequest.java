package com.guardian.order_service.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateOrderRequest {
    private UUID productId;
    private Integer quantity;
}
