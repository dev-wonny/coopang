package com.coopang.hub.presentation.request.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateCompanyRequestDto {
    @NotNull
    private UUID hubId;

    @NotNull
    private UUID companyManagerId;

    @NotBlank
    private String companyName;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String address1;

    @NotBlank
    private String address2;
}