package com.coopang.hub.presentation.request.shipper;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipperTypeRequestDto {
    @NotBlank(message = "Shipper Type is required.")
    private String shipperType;
}
