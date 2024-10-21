package com.coopang.order.presentation.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateOrderRequestDto {
    @NotNull
    private UUID nearHubId;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String address1;

    @NotBlank
    private String address2;
}
