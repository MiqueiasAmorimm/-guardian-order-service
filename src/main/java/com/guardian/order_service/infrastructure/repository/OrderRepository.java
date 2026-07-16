package com.guardian.order_service.infrastructure.repository;

import com.guardian.order_service.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order,UUID> {
}
