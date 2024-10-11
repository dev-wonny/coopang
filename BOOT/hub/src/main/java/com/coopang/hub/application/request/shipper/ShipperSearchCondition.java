package com.coopang.hub.application.request.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ShipperSearchCondition {
    private UUID shipperId;
    private UUID hubId;
    private ShipperTypeEnum shipperType;

    private String hubName;//starsWith
    private Boolean isDeleted;

    @Builder
    private ShipperSearchCondition(UUID shipperId, UUID hubId, ShipperTypeEnum shipperType, String hubName, Boolean isDeleted) {
        this.shipperId = shipperId;
        this.hubId = hubId;
        this.shipperType = shipperType;
        this.hubName = hubName;
        this.isDeleted = isDeleted;
    }

    public static ShipperSearchCondition from(UUID shipperId, UUID hubId, String shipperType, String hubName, Boolean isDeleted) {
        return ShipperSearchCondition.builder()
                .shipperId(shipperId)
                .hubId(hubId)
                .shipperType(ShipperTypeEnum.getRoleEnum(shipperType))
                .hubName(hubName)
                .isDeleted(isDeleted != null ? isDeleted : false)
                .build();
    }
}