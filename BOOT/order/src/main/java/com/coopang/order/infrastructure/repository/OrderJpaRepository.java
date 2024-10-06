package com.coopang.order.infrastructure.repository;

import com.coopang.order.domain.entity.order.OrderEntity;
import com.coopang.order.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID>, OrderRepository {
}
