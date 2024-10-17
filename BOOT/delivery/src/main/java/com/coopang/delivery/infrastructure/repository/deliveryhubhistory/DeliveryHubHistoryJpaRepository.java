package com.coopang.delivery.infrastructure.repository.deliveryhubhistory;

import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;
import com.coopang.delivery.domain.repository.deliveryhubhistory.DeliveryHubHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryHubHistoryJpaRepository extends JpaRepository<DeliveryHubHistoryEntity, UUID>, DeliveryHubHistoryRepositoryCustom, DeliveryHubHistoryRepository {
}
