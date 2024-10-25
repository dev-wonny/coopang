package com.coopang.order.infrastructure.repository.order;

import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.domain.repository.order.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID>, OrderRepository, OrderRepositoryCustom {
    Optional<OrderEntity> findById(UUID orderId);
}
