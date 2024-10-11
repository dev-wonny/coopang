package com.coopang.delivery.infrastructure.repository.delivery;

import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.domain.repository.delivery.DeliveryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, UUID>, DeliveryRepository, DeliveryRepositoryCustom {
}
