package com.coopang.delivery.infrastructure.repository.deliveryuserhistory;

import com.coopang.delivery.domain.entity.deliveryuserhistory.DeliveryUserHistoryEntity;
import com.coopang.delivery.domain.repository.deliveryuserhistory.DeliveryUserHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryUserHistoryJpaRepository extends JpaRepository<DeliveryUserHistoryEntity, UUID> ,DeliveryUserHistoryRepositoryCustom, DeliveryUserHistoryRepository {
}
