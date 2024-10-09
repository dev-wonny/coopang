package com.coopang.hub.application.request.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class ShipperDto {
    private UUID shipperId;
    private UUID hubId;
    private ShipperTypeEnum shipperType;
}
