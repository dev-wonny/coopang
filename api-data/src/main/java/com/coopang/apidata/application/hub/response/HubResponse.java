package com.coopang.apidata.application.hub.response;

import com.coopang.apidata.application.address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubResponse {
    private UUID hubId;
    private String hubName;
    private UUID hubManagerId;
    private Address hubAddress;
    private boolean isDeleted;
}
