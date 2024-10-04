package com.coopang.delivery.presentation.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DeliveryRequestDto {
    private UUID orderId;
    private UUID departureHubId;
    private UUID destinationHubId;
    private String zipCode;
    private String address1;
    private String address2;
    private UUID hubShipperId;
}
