package com.coopang.hub.presentation.request.shipper;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ShipperSearchConditionRequest {
    private UUID shipperId;
    private UUID hubId;
    private String shipperType;

    private String hubName;
    private Boolean isDeleted;
}
