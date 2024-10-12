package com.coopang.hub.presentation.request.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCompanyRequestDto {
    @NotNull(message = "Hub Id is required")
    private UUID hubId;

    @NotNull(message = "Company Manager Id is required")
    private UUID companyManagerId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Company zipCode is required")
    private String zipCode;

    @NotBlank(message = "Company address1 is required")
    private String address1;

    @NotBlank(message = "Company address2 is required")
    private String address2;

}
