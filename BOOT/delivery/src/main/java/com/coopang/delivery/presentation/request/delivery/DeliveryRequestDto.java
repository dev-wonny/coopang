package com.coopang.delivery.presentation.request.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeliveryRequestDto {
    @NotNull
    private UUID orderId;
    @NotNull(message = "Departure Hub ID is required")
    private UUID departureHubId;
    @NotNull(message = "Destination Hub ID is required")
    private UUID destinationHubId;
    @NotBlank
    private String zipCode;
    @NotBlank
    private String address1;
    @NotBlank
    private String address2;
}
