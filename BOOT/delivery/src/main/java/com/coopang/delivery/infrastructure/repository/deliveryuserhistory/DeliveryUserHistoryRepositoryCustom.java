package com.coopang.delivery.infrastructure.repository.deliveryuserhistory;

import com.coopang.delivery.domain.entity.deliveryuserhistory.DeliveryUserHistoryEntity;

import java.util.List;
import java.util.UUID;

public interface DeliveryUserHistoryRepositoryCustom {

    List<DeliveryUserHistoryEntity> findByDeliveryId(UUID deliveryId);
}
