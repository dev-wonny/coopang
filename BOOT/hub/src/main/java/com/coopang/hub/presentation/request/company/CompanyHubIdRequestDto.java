package com.coopang.hub.presentation.request.company;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyHubIdRequestDto {
    @NotNull
    private UUID hubId;
}