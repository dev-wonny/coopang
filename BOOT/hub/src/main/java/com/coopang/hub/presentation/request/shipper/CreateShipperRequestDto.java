package com.coopang.hub.presentation.request.shipper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateShipperRequestDto {
    @NotNull(message = "Shipper Id is required")
    private UUID shipperId;

    @NotNull(message = "Hub Id is required")
    private UUID hubId;

    @NotBlank(message = "Shipper type is required")
    private String shipperType;
}
