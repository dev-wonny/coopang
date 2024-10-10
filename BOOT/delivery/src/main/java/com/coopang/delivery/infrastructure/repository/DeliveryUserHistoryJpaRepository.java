package com.coopang.delivery.infrastructure.repository;

import com.coopang.delivery.domain.entity.deliveryuserhistory.DeliveryUserHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryUserHistoryJpaRepository extends JpaRepository<DeliveryUserHistoryEntity, UUID> {
}
