package com.coopang.delivery.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryRequestDto {
    private UUID orderId;
    @NotNull(message = "Departure Hub ID is required")
    private UUID departureHubId;
    @NotNull(message = "Destination Hub ID is required")
    private UUID destinationHubId;
    private String zipCode;
    private String address1;
    private String address2;
    private UUID hubShipperId;
}
