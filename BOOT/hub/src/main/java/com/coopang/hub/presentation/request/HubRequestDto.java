package com.coopang.hub.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class HubRequestDto {
    @NotBlank(message = "Hub name is required")
    private String hubName;

    private UUID hubManagerId;

    @NotBlank(message = "Hub zipCode is required")
    private String zipCode;

    @NotBlank(message = "Hub address1 is required")
    private String address1;

    @NotBlank(message = "Hub address2 is required")
    private String address2;
}
