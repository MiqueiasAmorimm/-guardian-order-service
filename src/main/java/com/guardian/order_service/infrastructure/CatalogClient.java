package com.guardian.order_service.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;

@Component

public class CatalogClient {
    @Value("${catalog.service.url}")
    private String catalogServiceUrl;
    private final RestTemplate restTemplate;

    public CatalogClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public boolean productExists(UUID productId){
        try {
            restTemplate.getForObject(
                    catalogServiceUrl + "/products/" + productId,
                    Object.class
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}