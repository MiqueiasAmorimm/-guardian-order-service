package com.guardian.order_service.domain.model;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table (name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Order() {
        // for JPA
    }
    public Order(UUID productId, Integer quantity, String status) {

        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();


    }
}


