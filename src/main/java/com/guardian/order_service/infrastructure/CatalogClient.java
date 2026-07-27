package com.guardian.order_service.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class CatalogClient {
    private final RestTemplate restTemplate;

    public CatalogClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public boolean productExists(UUID productId){
        try {
            restTemplate.getForObject(
                    "http://localhost:8081/products/" + productId,
                    Object.class
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}