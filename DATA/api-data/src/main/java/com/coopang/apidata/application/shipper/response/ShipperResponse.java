package com.coopang.apidata.application.shipper.response;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipperResponse {
    private UUID shipperId;
    private UUID hubId;
    private ShipperTypeEnum shipperType;

    private boolean isDeleted;
}