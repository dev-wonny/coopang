package com.coopang.order.domain.repository.order;

import com.coopang.order.domain.entity.order.OrderEntity;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<OrderEntity> findById(UUID orderId);
}
