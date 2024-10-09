package com.coopang.hub.presentation.request.shipper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateShipperRequestDto {
    @NotNull
    private UUID hubId;
    @NotBlank
    private String shipperType;
}
