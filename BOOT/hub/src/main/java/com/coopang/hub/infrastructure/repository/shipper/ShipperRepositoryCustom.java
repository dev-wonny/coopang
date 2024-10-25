package com.coopang.hub.infrastructure.repository.shipper;

import com.coopang.hub.application.request.shipper.ShipperSearchConditionDto;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipperRepositoryCustom {
    Page<ShipperEntity> search(ShipperSearchConditionDto condition, Pageable pageable);

    List<ShipperEntity> findShipperList(ShipperSearchConditionDto condition);
}
