package com.coopang.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressRequestDto {
    @NotBlank
    private String zipCode;

    @NotBlank
    private String address1;

    @NotBlank
    private String address2;
}
