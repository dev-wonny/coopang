package com.coopang.hub.domain.service.shipper;

import com.coopang.hub.application.request.shipper.ShipperDto;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import com.coopang.hub.infrastructure.repository.shipper.ShipperJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShipperDomainService {
    private final ShipperJpaRepository shipperJpaRepository;

    public ShipperDomainService(ShipperJpaRepository shipperJpaRepository) {
        this.shipperJpaRepository = shipperJpaRepository;
    }

    public ShipperEntity createShipper(ShipperDto dto) {
        ShipperEntity shipperEntity = ShipperEntity.create(
            dto.getShipperId()
            , dto.getHubId()
            , dto.getShipperType()

        );
        return shipperJpaRepository.save(shipperEntity);
    }
}