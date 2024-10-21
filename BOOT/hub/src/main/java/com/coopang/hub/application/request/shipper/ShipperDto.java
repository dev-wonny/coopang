package com.coopang.hub.application.request.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ShipperDto {
    private UUID shipperId;
    private UUID hubId;
    private ShipperTypeEnum shipperType;

    @Builder
    private ShipperDto(UUID shipperId, UUID hubId, ShipperTypeEnum shipperType) {
        this.shipperId = shipperId;
        this.hubId = hubId;
        this.shipperType = shipperType;
    }

    public static ShipperDto of(
        UUID shipperId
        , UUID hubId
        , ShipperTypeEnum shipperType
    ) {
        return ShipperDto.builder()
            .shipperId(shipperId)
            .hubId(hubId)
            .shipperType(shipperType)
            .build();
    }

    @Override
    public String toString() {
        return "ShipperDto{" +
            "shipperId=" + shipperId +
            ", hubId=" + hubId +
            ", shipperType=" + shipperType +
            '}';
    }

    public void createId(UUID userId) {
        this.shipperId = userId;
    }
}
