package com.coopang.order.application;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompleteDelivery {
    private UUID orderId;
    private UUID hubId;
    private UUID nearHubId;
}
