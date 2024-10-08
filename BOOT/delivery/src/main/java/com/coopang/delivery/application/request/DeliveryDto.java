package com.coopang.delivery.application.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryDto {
    private UUID orderId;
    private UUID departureHubId;
    private UUID destinationHubId;
    private String zipCode;
    private String address1;
    private String address2;
    private UUID hubShipperId;
}
