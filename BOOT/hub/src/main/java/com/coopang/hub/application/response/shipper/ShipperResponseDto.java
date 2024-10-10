package com.coopang.hub.application.response.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ShipperResponseDto {
    private UUID shipperId;
    private UUID hubId;
    private ShipperTypeEnum shipperType;

    private boolean isDeleted;

    private ShipperResponseDto(UUID shipperId, UUID hubId, ShipperTypeEnum shipperType, boolean isDeleted) {
        this.shipperId = shipperId;
        this.hubId = hubId;
        this.shipperType = shipperType;
        this.isDeleted = isDeleted;
    }

    public static ShipperResponseDto fromShipper(ShipperEntity shipper) {
        return new ShipperResponseDto(
                shipper.getShipperId(),
                shipper.getHubId(),
                shipper.getShipperType(),
                shipper.isDeleted()
        );
    }
}
