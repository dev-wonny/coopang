package com.coopang.delivery.domain.repository.deliveryuserhistory;

import com.coopang.delivery.domain.entity.deliveryuserhistory.DeliveryUserHistoryEntity;

import java.util.List;
import java.util.UUID;

public interface DeliveryUserHistoryRepository {

    List<DeliveryUserHistoryEntity> findByDeliveryId(UUID deliveryId);
}
