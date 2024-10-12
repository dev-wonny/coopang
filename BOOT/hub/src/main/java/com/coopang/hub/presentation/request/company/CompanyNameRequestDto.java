package com.coopang.hub.presentation.request.company;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyNameRequestDto {
    @NotBlank
    private String companyName;
}
