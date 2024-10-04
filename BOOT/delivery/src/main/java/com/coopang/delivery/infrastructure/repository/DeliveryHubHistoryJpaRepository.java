package com.coopang.delivery.infrastructure.repository;

import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryHubHistoryJpaRepository extends JpaRepository<DeliveryHubHistoryEntity, UUID> {
}
