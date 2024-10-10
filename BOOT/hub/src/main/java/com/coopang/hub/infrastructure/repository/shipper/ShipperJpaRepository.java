package com.coopang.hub.infrastructure.repository.shipper;

import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import com.coopang.hub.domain.repository.shipper.ShipperRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipperJpaRepository extends JpaRepository<ShipperEntity, UUID>, ShipperRepository, ShipperRepositoryCustom {
}
