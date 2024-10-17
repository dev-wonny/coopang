package com.coopang.delivery.presentation.request.delivery;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliverySearchCondition {
    private UUID destinationHubId;
}
