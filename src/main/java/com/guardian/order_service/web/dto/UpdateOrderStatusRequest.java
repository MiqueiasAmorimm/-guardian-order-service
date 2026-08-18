package com.guardian.order_service.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    @NotBlank
    private String status;

}
