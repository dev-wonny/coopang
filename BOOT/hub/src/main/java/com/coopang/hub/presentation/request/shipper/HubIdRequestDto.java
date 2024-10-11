package com.coopang.hub.presentation.request.shipper;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class HubIdRequestDto {
    @NotNull(message = "hubId id is required.")
    private UUID hubId;
}
