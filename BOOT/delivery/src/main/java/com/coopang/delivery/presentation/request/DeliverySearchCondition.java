package com.coopang.delivery.presentation.request;

import lombok.Data;

import java.util.UUID;

@Data
public class DeliverySearchCondition {
    private UUID destinationHubId;
}
