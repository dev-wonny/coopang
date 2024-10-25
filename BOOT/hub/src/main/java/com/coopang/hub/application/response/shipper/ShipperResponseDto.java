package com.coopang.hub.application.response.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import lombok.Builder;
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

    @Builder
    private ShipperResponseDto(UUID shipperId, UUID hubId, ShipperTypeEnum shipperType, boolean isDeleted) {
        this.shipperId = shipperId;
        this.hubId = hubId;
        this.shipperType = shipperType;
        this.isDeleted = isDeleted;
    }

    public static ShipperResponseDto fromShipper(ShipperEntity shipper) {
        return ShipperResponseDto
            .builder()
            .shipperId(shipper.getShipperId())
            .hubId(shipper.getHubId())
            .shipperType(shipper.getShipperType())
            .isDeleted(shipper.isDeleted())
            .build();
    }
}
