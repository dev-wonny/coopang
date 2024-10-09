package com.coopang.hub.presentation.request.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateHubRequestDto {
    @NotBlank(message = "Hub name is required")
    private String hubName;

    @NotNull(message = "Hub Manager Id is required")
    private UUID hubManagerId;

    @NotBlank(message = "Hub zipCode is required")
    private String zipCode;

    @NotBlank(message = "Hub address1 is required")
    private String address1;

    @NotBlank(message = "Hub address2 is required")
    private String address2;
}
