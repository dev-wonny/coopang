package com.coopang.delivery.infrastructure.repository.deliveryhubhistory;

import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryHubHistoryRepositoryCustom {

    List<DeliveryHubHistoryEntity> findByDeliveryId(UUID deliveryId);
}
