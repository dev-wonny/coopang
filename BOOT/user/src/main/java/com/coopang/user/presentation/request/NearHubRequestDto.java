package com.coopang.user.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NearHubRequestDto {
    @NotNull(message = "HubId is required.")
    private UUID nearHubId;
}
