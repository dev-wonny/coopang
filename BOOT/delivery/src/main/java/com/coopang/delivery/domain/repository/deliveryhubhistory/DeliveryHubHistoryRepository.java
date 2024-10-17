package com.coopang.delivery.domain.repository.deliveryhubhistory;

import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;

import java.util.List;
import java.util.UUID;

public interface DeliveryHubHistoryRepository {

    List<DeliveryHubHistoryEntity> findByDeliveryId(UUID deliveryId);
}
