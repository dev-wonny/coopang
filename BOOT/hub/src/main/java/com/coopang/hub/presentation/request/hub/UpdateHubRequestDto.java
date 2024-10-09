package com.coopang.hub.presentation.request.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateHubRequestDto {
    @NotBlank
    private String hubName;

    @NotNull
    private UUID hubManagerId;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String address1;

    @NotBlank
    private String address2;
}
